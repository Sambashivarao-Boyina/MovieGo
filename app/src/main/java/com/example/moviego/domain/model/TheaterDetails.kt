package com.example.moviego.domain.model

data class TheaterDetails(
    val _id: String,
    val address: String,
    val admin: String,
    val city: String,
    val image: String,
    val contactNumber: String,
    val name: String,
    val pincode: String,
    val screens: List<Screen>,
    val state: String,
    val location: Location
)