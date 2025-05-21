package com.example.moviego.domain.model

data class Booking(
    val _id: String,
    val show: Show,
    val seats:List<Seat>,
    val user: User,
    val bookingStatus: String,
    val createdAt: String,
    val paymentId: String?,
    val ticketCost:Float,
    val totalBookingCost: Float
)
