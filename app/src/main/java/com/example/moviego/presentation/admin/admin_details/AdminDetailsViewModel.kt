package com.example.moviego.presentation.admin.admin_details

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.moviego.domain.usecases.admin_usecases.AdminUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AdminDetailsViewModel @Inject constructor(
    private val adminUseCases: AdminUseCases
): ViewModel() {
    var sideEffect by mutableStateOf<String?>(null)
        private set

    var isLoading by mutableStateOf(false)
        private set

    fun onEvent(event: AdminDetailsEvent) {
        when(event) {
            AdminDetailsEvent.RemoveSideEffect -> {
                sideEffect = null
            }
        }
    }



}

sealed class AdminDetailsEvent {
    data object RemoveSideEffect: AdminDetailsEvent()
}