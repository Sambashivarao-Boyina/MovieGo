package com.example.moviego.presentation.user.payment_confirmation

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviego.domain.model.Booking
import com.example.moviego.domain.usecases.user_usecases.UserUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
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
}

sealed class UserPaymentConfirmationEvent {
    data object RemoveSideEffect: UserPaymentConfirmationEvent()
    data object CancelBooking: UserPaymentConfirmationEvent()
}