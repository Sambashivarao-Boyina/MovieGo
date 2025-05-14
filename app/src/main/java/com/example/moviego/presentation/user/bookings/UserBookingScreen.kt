package com.example.moviego.presentation.user.bookings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.moviego.R
import com.example.moviego.domain.model.Booking
import com.example.moviego.ui.theme.RedE31

@Composable
fun UserBookingsScreen(
    isLoading: Boolean,
    bookings: List<Booking>,
    onEvent:(UserBookingsEvent) -> Unit
) {
    if(isLoading) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator()
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(bookings) {
                BookingCard(it)
            }
        }
    }
}

@Composable
fun BookingCard(booking: Booking) {
    Card(
        elevation = CardDefaults.elevatedCardElevation(8.dp),
        colors = CardDefaults.cardColors().copy(
            containerColor = MaterialTheme.colorScheme.surface
        ),
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
        ) {
            Column {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(booking.show.movie.Poster)
                        .placeholder(R.drawable.placeholder)
                        .error(R.drawable.placeholder)
                        .build(),
                    contentDescription = null,
                    modifier = Modifier.fillMaxWidth(1f)
                )
                Text(text = booking.show.movie.Title)
            }
            Row {
                Text(text = booking.show.movie.Year)
                Text(text = booking.show.movie.Genre)
            }

            Row {
                Icon(
                    painter = painterResource(R.drawable.location),
                    tint = RedE31,
                    contentDescription = null
                )
                Text(
                    text = "${booking.show.theater.name} ${booking.show.theater.city}"
                )
            }

            Text(text = "${booking.show.movie.imdbRating}")
        }
    }
}