package com.example.moviego.presentation.user.bookings

import androidx.lifecycle.ViewModel
import com.example.moviego.domain.usecases.user_usecases.UserUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UserBookingViewModel @Inject constructor(
    private val userUserUseCases: UserUseCases
): ViewModel() {

}