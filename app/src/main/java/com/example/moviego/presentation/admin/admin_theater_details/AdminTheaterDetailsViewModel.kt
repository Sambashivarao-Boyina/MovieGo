package com.example.moviego.presentation.admin.admin_theater_details

import android.view.View
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
class AdminTheaterDetailsViewModel @Inject constructor(
    private val adminUseCases: AdminUseCases
): ViewModel() {
    var theaterId by mutableStateOf<String?>(null)
        private set

    var theaterDetails by mutableStateOf<TheaterDetails?>(null)
        private set

    var sideEffect by mutableStateOf<String?>(null)
        private set

    var isLoading by mutableStateOf(false)
        private set

    fun initializeTheater(theaterId: String) {
        this.theaterId = theaterId
        loadTheater()
    }

    private fun loadTheater() {
        viewModelScope.launch {
            isLoading = true
            theaterId?.let {
                val result = adminUseCases.getTheaterDetails(theaterId!!)
                if(result.isSuccess) {
                    theaterDetails = result.getOrNull()
                } else {
                    sideEffect = result.exceptionOrNull()?.message
                }
            }
            isLoading = false
        }
    }



}