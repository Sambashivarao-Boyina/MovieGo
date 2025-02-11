package com.example.moviego.domain.model

data class Movie(
    val _id: String,
    val duration: Int,
    val language: String,
    val posterUrl: String,
    val title: String
)