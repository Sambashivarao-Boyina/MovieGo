package com.example.moviego.data.remote.Dao

data class NewShow(
    val movie: String,
    val theater: String,
    val screen: String,
    val ticketCost : Int,
    val showTime: String,
    val date: String
)
