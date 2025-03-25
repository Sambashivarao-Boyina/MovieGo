package com.example.moviego.presentation.admin.theaters

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviego.domain.model.TheaterDetails
import com.example.moviego.domain.usecases.admin_usecases.AdminUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminTheatersViewModel @Inject constructor(
    private val adminUseCases: AdminUseCases
): ViewModel() {
    var theaters by mutableStateOf<List<TheaterDetails>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)
        private set

    var sideEffect by mutableStateOf<String?>(null)
        private set

    init {
        getTheaters()
    }

    fun onEvent(event: AdminTheatersEvent) {
        when(event) {
            AdminTheatersEvent.RemoveSideEffect -> {
                sideEffect = null
            }
        }
    }

    private fun getTheaters() {
        viewModelScope.launch {
            isLoading = true
            val result = adminUseCases.getAllAdminTheaters()
            if(result.isSuccess) {
                theaters = result.getOrElse { emptyList() }
            } else {
                sideEffect = result.exceptionOrNull()?.message
            }
            isLoading = false
        }
    }

}

sealed class AdminTheatersEvent {
    data object RemoveSideEffect: AdminTheatersEvent()
}