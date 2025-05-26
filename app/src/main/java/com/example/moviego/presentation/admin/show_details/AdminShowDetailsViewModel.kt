package com.example.moviego.presentation.admin.show_details

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.moviego.domain.model.Admin
import com.example.moviego.domain.model.ShowDetails
import com.example.moviego.domain.usecases.admin_usecases.AdminUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminShowDetailsViewModel @Inject constructor(
    private val adminUseCases: AdminUseCases
) : ViewModel() {
    var showDetails by mutableStateOf<ShowDetails?>(null)
        private set
    var isLoading by mutableStateOf(false)
        private set
    var sideEffect by mutableStateOf<String?>(null)
        private set
    var updatingShowStatus by mutableStateOf(false)
        private set

    fun loadShow(showId:String) {
        isLoading = true
        viewModelScope.launch {
            val result = adminUseCases.getShowDetails(showId)
            if(result.isSuccess) {
                showDetails = result.getOrDefault(null)
            } else {
                sideEffect = result.exceptionOrNull()?.message
            }
        }
        isLoading = false
    }

    fun onEvent(event: AdminShowDetailsEvent) {
        when(event) {
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
                if(result.isSuccess) {
                    showDetails = result.getOrDefault(null)
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
                if(result.isSuccess) {
                    showDetails = result.getOrDefault(null)
                } else {
                    sideEffect = result.exceptionOrNull()?.message
                }
            }
        }
        updatingShowStatus = false
    }

}

sealed class AdminShowDetailsEvent {
    data object RemoveSideEffect: AdminShowDetailsEvent()
    data object OpenShow: AdminShowDetailsEvent()
    data object CloseShow: AdminShowDetailsEvent()
}