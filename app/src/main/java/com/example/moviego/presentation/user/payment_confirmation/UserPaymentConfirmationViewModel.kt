package com.example.moviego.presentation.user.payment_confirmation

import android.app.Activity
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviego.domain.model.Booking
import com.example.moviego.domain.usecases.user_usecases.UserUseCases
import com.example.moviego.util.Constants.RAZOR_PAY_KEY_ID
import com.razorpay.Checkout
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class UserPaymentConfirmationViewModel @Inject constructor(
    private val userUseCases: UserUseCases
): ViewModel() {
    var bookingId by mutableStateOf<String?>(null)
        private set

    var booking by mutableStateOf<Booking?>(null)
        private set

    var cancelingBooking by mutableStateOf(false)
        private set

    var isLoading by mutableStateOf(false)
        private set

    var sideEffect by mutableStateOf<String?>(null)
        private set

    var navigateBack by mutableStateOf(false)
        private set

    var ticketsPrice by mutableFloatStateOf(0F)
        private set

    val convenienceFees by derivedStateOf {
        ticketsPrice / 10
    }

    val totalPayment by derivedStateOf {
        ticketsPrice + convenienceFees
    }

    var paymentState by mutableStateOf<PaymentState>(PaymentState.Idle)
        private set



    fun initializeBookingId(bookingId: String) {
        this.bookingId = bookingId
        loadBooking()
    }

    private fun loadBooking() {
        viewModelScope.launch {
            isLoading = true
            if(bookingId != null) {
                val result = userUseCases.getBookingDetails(bookingId!!)
                if(result.isSuccess) {
                    booking = result.getOrNull()
                    booking?.let {
                        ticketsPrice = (booking!!.show.ticketCost * booking!!.seats.size).toFloat()
                    }
                } else {
                    sideEffect = result.exceptionOrNull()?.message
                }
            }
            isLoading = false
        }
    }

    fun onEvent(event: UserPaymentConfirmationEvent) {
        when(event) {
            UserPaymentConfirmationEvent.RemoveSideEffect -> {
                sideEffect = null
            }
            UserPaymentConfirmationEvent.CancelBooking -> {
                cancelBooking()
            }
            is UserPaymentConfirmationEvent.StartPayment -> {
                startPayment(activity = event.activity)
            }
            UserPaymentConfirmationEvent.ResetConfirmation -> {
                paymentState = PaymentState.Idle
            }
        }
    }

    private fun cancelBooking() {
        cancelingBooking = true
        viewModelScope.launch {
            if(bookingId != null) {
                val result = userUseCases.cancelBooking(bookingId!!)
                if(result.isSuccess) {
                    sideEffect = result.getOrNull()
                    navigateBack = true
                } else {
                    sideEffect = result.exceptionOrNull()?.message
                }
            }
        }
        cancelingBooking = false
    }

    private fun startPayment(activity: Activity, description: String = "test payment") {
        try{

            val options = JSONObject().apply {
                put("name","MovieGo")
                put("description",description)
                put("currency","INR")
                put("amount", (totalPayment * 100).toLong())
                put("theme","#bb000e")
                put("method",JSONObject().apply {
                    put("upi",true)
                    put("qr",true)
                })
                put("upi",JSONObject().apply {
                    put("flow","intent")
                })
                put("readonly",JSONObject().apply {
                    put("contact",true)
                    put("email",true)
                    put("method",false)
                })
                put("notes", JSONObject().apply {
                    put("bookingId", bookingId)
                })
            }

            val checkout = Checkout()
            checkout.setKeyID(RAZOR_PAY_KEY_ID)
            checkout.open(activity, options)
        }catch (error: Exception) {
            paymentState = PaymentState.Error(error.message.toString())
        }
    }

     fun handlePaymentSuccess(paymentId: String) {
        viewModelScope.launch {
            val result = userUseCases.checkoutBooking(paymentId)
            if(result.isSuccess) {
                sideEffect = result.getOrDefault("Payment Successful")
                paymentState = PaymentState.Success(paymentId)
            } else {
                sideEffect = result.exceptionOrNull()?.message
                paymentState = PaymentState.Error(sideEffect.toString())
            }
        }
    }

     fun handlePaymentError(message: String){
        viewModelScope.launch {
            paymentState = PaymentState.Error(message)
            cancelBooking()
            sideEffect = message

        }
    }
}

sealed class UserPaymentConfirmationEvent {
    data object RemoveSideEffect: UserPaymentConfirmationEvent()
    data object CancelBooking: UserPaymentConfirmationEvent()
    data class StartPayment(val activity: Activity): UserPaymentConfirmationEvent()
    data object ResetConfirmation: UserPaymentConfirmationEvent()
}

sealed class PaymentState {
    data object Idle: PaymentState()
    data class Success(val paymentId: String): PaymentState()
    data class Error(val message: String): PaymentState()
}