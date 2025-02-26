package com.example.moviego.domain.model

data class Seat(
    val _id: String,
    val bookedBy: Any,
    val processingUntil: Any,
    val seatCode: String,
    val status: String
)