package com.example.moviego.presentation.admin.show_details

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

    fun loadShow(showId:String) {
        viewModelScope.launch {
            val result = adminUseCases.getShowDetails(showId)
            if(result.isSuccess) {
                showDetails = result.getOrDefault(null)
            } else {
                sideEffect = result.exceptionOrNull()?.message
            }
        }
    }

    fun onEvent(event: AdminShowDetailsEvent) {
        when(event) {
            AdminShowDetailsEvent.RemoveSideEffect -> {
                sideEffect = null
            }
        }
    }

}

sealed class AdminShowDetailsEvent {
    data object RemoveSideEffect: AdminShowDetailsEvent()
}