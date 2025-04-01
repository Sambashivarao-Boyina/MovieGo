package com.example.moviego.presentation.user.show_booking

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.moviego.domain.model.ShowDetails

@Composable
fun ShowBookingScreen(
    showDetails: ShowDetails?,
    isLoading: Boolean,
    onEvent: (ShowBookingEvent) -> Unit
) {
    Text(text="show details")
}