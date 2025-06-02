package com.example.moviego.presentation.admin.show_details

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviego.domain.model.Booking
import com.example.moviego.domain.model.ShowDetails
import com.example.moviego.domain.usecases.admin_usecases.AdminUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminShowDetailsViewModel @Inject constructor(
    private val adminUseCases: AdminUseCases
) : ViewModel() {
    var showId by mutableStateOf<String?>(null)
        private set
    var isBookingsLoading by mutableStateOf(false)
        private set
    var bookings by mutableStateOf<List<Booking>>(emptyList())
        private set
    var showDetails by mutableStateOf<ShowDetails?>(null)
        private set
    var isLoading by mutableStateOf(false)
        private set
    var sideEffect by mutableStateOf<String?>(null)
        private set
    var updatingShowStatus by mutableStateOf(false)
        private set

    fun initializeShowId(showId: String) {
        this.showId = showId
        loadShow()
        loadBookingsOfShow()
    }

    private fun loadShow() {
        showId?.let {
            isLoading = true
            viewModelScope.launch {
                val result = adminUseCases.getShowDetails(it)
                if (result.isSuccess) {
                    showDetails = result.getOrDefault(null)
                } else {
                    sideEffect = result.exceptionOrNull()?.message
                }
            }
            isLoading = false
        }
    }

    fun onEvent(event: AdminShowDetailsEvent) {
        when (event) {
            AdminShowDetailsEvent.RemoveSideEffect -> {
                sideEffect = null
            }
            AdminShowDetailsEvent.CloseShow -> {
                closeShow()
            }
            AdminShowDetailsEvent.OpenShow -> {
                openShow()
            }
        }
    }

    private fun openShow() {
        updatingShowStatus = true
        showDetails?.let {
            viewModelScope.launch {
                val result = adminUseCases.openShow(it._id)
                if (result.isSuccess) {
                    showDetails = result.getOrDefault(null)
                    sideEffect = "Show is Opened"
                } else {
                    sideEffect = result.exceptionOrNull()?.message
                }
            }
        }
        updatingShowStatus = false
    }

    private fun closeShow() {
        updatingShowStatus = true
        showDetails?.let {
            viewModelScope.launch {
                val result = adminUseCases.closeShow(it._id)
                if (result.isSuccess) {
                    showDetails = result.getOrDefault(null)
                    sideEffect = "Show is Closed"
                } else {
                    sideEffect = result.exceptionOrNull()?.message
                }
            }
        }
        updatingShowStatus = false
    }

    private fun loadBookingsOfShow() {
        showId?.let {
            isBookingsLoading = true
            viewModelScope.launch {
                val result = adminUseCases.getBookingsOfShow(it)
                if (result.isSuccess) {
                    bookings = result.getOrDefault(emptyList())
                } else {
                    sideEffect = result.exceptionOrNull()?.message
                }
            }
            isBookingsLoading = false
        }
    }

}

sealed class AdminShowDetailsEvent {
    data object RemoveSideEffect : AdminShowDetailsEvent()
    data object OpenShow : AdminShowDetailsEvent()
    data object CloseShow : AdminShowDetailsEvent()
}