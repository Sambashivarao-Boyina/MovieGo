package com.example.moviego.domain.model

data class Admin(
    val _id: String,
    val email: String,
    val movies: List<String>,
    val name: String,
    val phone: String,
    val role: String,
    val theaters: List<String>
)