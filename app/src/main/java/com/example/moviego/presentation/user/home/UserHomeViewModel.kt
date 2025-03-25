package com.example.moviego.presentation.user.home

import androidx.lifecycle.ViewModel
import com.example.moviego.domain.usecases.user_usecases.UserUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UserHomeViewModel @Inject constructor(
    private val userUserUseCases: UserUseCases
): ViewModel() {

}