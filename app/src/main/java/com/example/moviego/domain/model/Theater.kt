package com.example.moviego.domain.model

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
    val state: String
)