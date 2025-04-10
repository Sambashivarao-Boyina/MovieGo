package com.example.moviego.domain.model

data class ShowDetails(
    val _id: String,
    val admin: String,
    val bookedSeatsCount: Int,
    val date: String,
    val movie: Movie,
    val screen: Screen,
    val seats: List<Seat>,
    val showTime: String,
    val theater: Theater,
    val ticketCost: Int,
    val bookingStatus:String
)