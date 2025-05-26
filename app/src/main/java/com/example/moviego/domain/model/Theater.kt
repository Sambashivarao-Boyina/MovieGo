package com.example.moviego.domain.model

import com.example.moviego.presentation.admin.add_theater.AdminAddTheaterEvent

data class Theater(
    val _id: String,
    val address: String,
    val admin: String,
    val image: String,
    val city: String,
    val contactNumber: String,
    val name: String,
    val pincode: String,
    val screens: List<String>,
    val state: String,
    val location: Location
)

data class Location(
    val type: String,
    val coordinates: List<Double>
)