package com.example.moviego.presentation.user.booking_details

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviego.domain.model.BookingDetails
import com.example.moviego.domain.usecases.user_usecases.UserUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookingDetailsViewModel @Inject constructor(
    private val userUseCases: UserUseCases
): ViewModel() {
    var bookingId by mutableStateOf("")
        private set

    var isLoading by mutableStateOf(false)
        private set

    var sideEffect by mutableStateOf<String?>(null)
        private set

    var bookingDetails by mutableStateOf<BookingDetails?>(null)
        private set

    fun onEvent(event: BookingDetailsEvent) {
        when(event) {
            BookingDetailsEvent.RemoveSideEffect -> {
                sideEffect = null
            }
            is BookingDetailsEvent.InitializeBookingId -> {
                bookingId = event.bookingId;
                fetchBookingDetails()
            }
        }
    }

    private fun fetchBookingDetails() {
        isLoading = true
        viewModelScope.launch {
            bookingId?.let {
                val result = userUseCases.getBookingDetails(bookingId)
                if(result.isSuccess) {
                    bookingDetails = result.getOrNull()
                    if(bookingDetails == null) {
                        sideEffect = "Something went wrong"
                    }
                } else {
                    sideEffect = result.exceptionOrNull()?.message
                }
            }
        }
        isLoading = false
    }



}

sealed class BookingDetailsEvent {
    data object RemoveSideEffect: BookingDetailsEvent()
    data class InitializeBookingId(var bookingId: String): BookingDetailsEvent()
}