package com.example.moviego.presentation.admin.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.moviego.domain.model.Show

@Composable
fun ShowCard(
    show: Show
) {
    Card(
        modifier = Modifier.fillMaxWidth()
            .clickable {  },
        colors = CardDefaults.cardColors().copy(
            containerColor = MaterialTheme.colorScheme.surface
        ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Column(
                modifier = Modifier.weight(0.5f)
            ) {
                Text("Movie")
                Text("Theater")
                Text("Screen")
                Text("Date")
                Text("ShowTime")
                Text("Ticket Cost")
                Text("Tickets Booked")
            }
            Column(
                modifier = Modifier.weight(0.5f)
            ) {
                Text(show.movie.title)
                Text(show.theater.name)
                Text(show.screen.screenName)
                Text(show.date)
                Text(show.showTime)
                Text(show.ticketCost.toString())
                Text(show.bookedSeatsCount.toString())
            }
        }
    }
}