package com.example.moviego.presentation.user.bookings

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.moviego.R
import com.example.moviego.domain.model.Booking
import com.example.moviego.ui.theme.Black1C1
import com.example.moviego.ui.theme.RedE31
import java.util.Locale
import kotlin.math.sign

@Composable
fun UserBookingsScreen(
    isLoading: Boolean,
    bookings: List<Booking>,
    onEvent:(UserBookingsEvent) -> Unit
) {
    Scaffold {
        if(isLoading) {
            Column(
                modifier = Modifier.padding(it).fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                modifier = Modifier.padding(it).padding(horizontal = 10.dp).fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(13.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(bookings) {
                    BookingCard(it)
                }
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
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Black1C1)
                .padding(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth()
                    .height(100.dp),
                horizontalArrangement = Arrangement.spacedBy(20.dp),
                verticalAlignment = Alignment.Bottom
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(booking.show.movie.Poster)
                        .placeholder(R.drawable.placeholder)
                        .error(R.drawable.placeholder)
                        .build(),
                    contentDescription = null,
                    modifier = Modifier
                        .height(100.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .border(
                            width = 2.dp,
                            color = RedE31,
                            shape = RoundedCornerShape(10.dp)
                        )
                )
                Box(
                    modifier = Modifier.fillMaxHeight()
                        .fillMaxWidth()
                ){
                    Column(
                        modifier = Modifier.fillMaxHeight()
                            .padding(bottom = 20.dp),
                        verticalArrangement = Arrangement.Bottom

                    ) {
                        Text(text = booking.show.movie.Title, style = MaterialTheme.typography.titleLarge)
                        Spacer(modifier = Modifier.height(6.dp))
                        Row {
                            Icon(
                                painter = painterResource(R.drawable.location),
                                tint = RedE31,
                                contentDescription = null,
                                modifier = Modifier.size(20.dp)
                            )
                            Text(
                                text = "${booking.show.theater.name} ${booking.show.theater.city}",
                                color = Color.Gray
                            )
                        }
                    }

                    Row(
                        modifier = Modifier.align(Alignment.TopEnd)
                    ) {
                        BookingStatus(status = booking.bookingStatus)
                    }

                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                DataBlock(
                    title = "Date",
                    data = booking.show.date
                )
                DataBlock(
                    title = "Time",
                    data = booking.show.showTime
                )
                DataBlock(
                    title = "Screen",
                    data = booking.show.screen.screenName
                )

            }

            Spacer(Modifier.height(10.dp))
            HorizontalDivider()
            Spacer(Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.seat),
                        modifier = Modifier.size(20.dp),
                        contentDescription = null,
                        tint = RedE31
                    )
                    Text("Seats: ", color = RedE31)
                    booking.seats.map {
                        Text("${it.seatCode}", color = RedE31, fontWeight = FontWeight.Bold)
                    }
                }

                Text(
                    text = "₹${booking.seats.size * booking.show.ticketCost + (booking.seats.size * booking.show.ticketCost) / 10 }",
                    color = RedE31,
                    fontWeight = FontWeight.Bold
                )
            }

        }
    }
}


@Composable
fun DataBlock(
    title: String,
    data: String
) {
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .border(
                width = 0.3.dp,
                color = Color.White,
                shape = RoundedCornerShape(10.dp)
            )
            .background(Color.White.copy(alpha = 0.04f))
            .padding(horizontal = 10.dp, vertical = 5.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Text(text = title, color = Color.Gray)
        Text(text = data, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun BookingStatus(status: String) {
    Card(
        elevation = CardDefaults.elevatedCardElevation(10.dp),
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(color = RedE31).padding(horizontal = 10.dp, vertical = 5.dp)

    ) {
        Text(text = status.uppercase(Locale.ROOT), color = Color.White, modifier = Modifier.background(RedE31), fontWeight = FontWeight.Bold)
    }
}