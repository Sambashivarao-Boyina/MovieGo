package com.example.moviego.presentation.user.show_booking

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviego.domain.model.ShowDetails
import com.example.moviego.domain.usecases.user_usecases.UserUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShowBookingViewModel @Inject constructor(
    private val userUseCases: UserUseCases
): ViewModel() {
    var showId by mutableStateOf("")
        private set

    var showDetails by mutableStateOf<ShowDetails?>(null)
        private set

    var sideEffect by mutableStateOf<String?>(null)
        private set

    var isLoading by mutableStateOf(false)
        private set

    fun initializeShowId(showId: String) {
        this.showId = showId
        loadShow()
    }

    fun onEvent(event: ShowBookingEvent) {
        when(event) {
            ShowBookingEvent.RemoveSideEffect -> {
                sideEffect = null
            }
        }
    }

    private fun loadShow() {
        isLoading = true
        viewModelScope.launch {
            val result = userUseCases.getShowDetailsForBooking(showId)
            if(result.isSuccess) {
                showDetails = result.getOrDefault(null)
            } else {
                sideEffect = result.exceptionOrNull()?.message
            }
        }
        isLoading = false
    }
}

sealed class ShowBookingEvent {
    data object RemoveSideEffect: ShowBookingEvent()
}