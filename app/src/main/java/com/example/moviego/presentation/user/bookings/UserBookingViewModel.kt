package com.example.moviego.presentation.user.bookings

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
class UserBookingViewModel @Inject constructor(
    private val userUserUseCases: UserUseCases
): ViewModel() {
    var bookingDetails by mutableStateOf<List<BookingDetails>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)
        private set

    var sideEffect by mutableStateOf<String?>(null)
        private set

    fun onEvent(event: UserBookingsEvent) {
        when(event) {
            UserBookingsEvent.RemoveSideEffect -> {
                sideEffect = null
            }
            UserBookingsEvent.ReloadData -> {
                getBookingsList()
            }
        }
    }

    init {
        getBookingsList()
    }

    private fun getBookingsList() {
        viewModelScope.launch {
            val result = userUserUseCases.getBookingsList()
            if(result.isSuccess) {
                bookingDetails = result.getOrDefault(emptyList())
            } else {
                sideEffect = result.exceptionOrNull()?.message
            }
        }
    }
}

sealed class UserBookingsEvent {
    data object RemoveSideEffect: UserBookingsEvent()
    data object ReloadData: UserBookingsEvent()
}