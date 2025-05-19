package com.example.moviego.presentation.user.booking_details

import android.widget.GridView
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.moviego.R
import com.example.moviego.domain.model.Booking
import com.example.moviego.presentation.admin.components.TopBar
import com.example.moviego.ui.theme.Black111
import com.example.moviego.ui.theme.Black161
import com.example.moviego.ui.theme.Black1C1
import com.example.moviego.ui.theme.RedBB0
import com.example.moviego.ui.theme.RedE31

@Composable
fun BookingDetailsScreen(
    isLoading: Boolean,
    booking: Booking?,
    navController: NavHostController
) {
    Scaffold(
        topBar = {
            TopBar(
                title = "Booking Details",
                navigationBox = {
                    IconButton(
                        onClick = {
                            navController.popBackStack()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = "back"
                        )
                    }
                }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .padding(
                    top = it.calculateTopPadding(),
                    start = it.calculateStartPadding(LayoutDirection.Ltr),
                    end = it.calculateEndPadding(LayoutDirection.Rtl)
                )
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (isLoading) {
                CircularProgressIndicator()
            } else {
                booking?.let {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        item {
                            Spacer(modifier = Modifier.height(20.dp))

                            AsyncImage(
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(booking.show.movie.Poster)
                                    .placeholder(R.drawable.placeholder)
                                    .error(R.drawable.placeholder)
                                    .build(),
                                contentDescription = null,
                                modifier = Modifier
                                    .height(300.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .border(
                                        width = 1.dp,
                                        color = RedE31,
                                        shape = RoundedCornerShape(20.dp)
                                    )
                            )
                        }
                        item {
                            Text(
                                text = booking.show.movie.Title,
                                style = MaterialTheme.typography.displaySmall
                            )
                            Text(
                                text = "${booking.show.movie.Year} ${booking.show.movie.Genre}",
                                color = Color.Gray
                            )
                        }

                        item {
                            Card(
                                elevation = CardDefaults.cardElevation(
                                    defaultElevation = 10.dp
                                ),
                                modifier = Modifier
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(
                                        color = Color.Gray.copy(alpha = 0.2f)
                                    )
                                    .padding(
                                        vertical = 10.dp,
                                        horizontal = 40.dp
                                    ),
                                colors = CardDefaults.cardColors(
                                    containerColor = Color.Transparent
                                )
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(7.dp)
                                ) {
                                    Icon(
                                        painter = painterResource(R.drawable.star),
                                        contentDescription = null,
                                        modifier = Modifier.size(20.dp),
                                        tint = Color.Yellow
                                    )
                                    Text(
                                        text = "${booking.show.movie.imdbRating}/10",
                                        modifier = Modifier.background(Color.Transparent),
                                        color = Color.Yellow,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }

                        item {
                            Spacer(Modifier.height(20.dp))
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth(0.9f)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(
                                        color = Color.Gray.copy(alpha = 0.1f)
                                    )
                                    .padding(20.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = Color.Transparent
                                )
                            ) {
                                Column(
                                    verticalArrangement = Arrangement.spacedBy(20.dp)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                                    ) {
                                        Icon(
                                            painter = painterResource(R.drawable.info),
                                            modifier = Modifier.size(20.dp),
                                            tint = RedE31,
                                            contentDescription = null
                                        )

                                        Text(
                                            text = "Show Information",
                                            color = RedE31,
                                            fontWeight = FontWeight.Bold,
                                            style = MaterialTheme.typography.titleLarge
                                        )
                                    }

                                    HorizontalDivider(
                                        color = RedE31
                                    )


                                    Column(
                                        verticalArrangement = Arrangement.spacedBy(10.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        DetailsContainer(
                                            title = "THEATER",
                                            value = "${booking.show.theater.name}"
                                        )
                                        DetailsContainer(
                                            title = "SCREEN",
                                            value = "${booking.show.screen.screenName}"
                                        )
                                        DetailsContainer(
                                            title = "DATE",
                                            value = "${booking.show.date}"
                                        )
                                        DetailsContainer(
                                            title = "TIME",
                                            value = "${booking.show.showTime}"
                                        )
                                        DetailsContainer(
                                            title = "SCREEN TYPE",
                                            value = "${booking.show.screen.screenType}"
                                        )
                                        DetailsContainer(
                                            title = "SOUND TYPE",
                                            value = "${booking.show.screen.soundType}"
                                        )
                                    }
                                }
                            }
                        }

                        item {
                            Spacer(Modifier.height(20.dp))
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth(0.9f)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(
                                        color = Color.Gray.copy(alpha = 0.1f)
                                    )
                                    .padding(20.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = Color.Transparent
                                )
                            ) {
                                Column(
                                    verticalArrangement = Arrangement.spacedBy(20.dp)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                                    ) {
                                        Icon(
                                            painter = painterResource(R.drawable.seat),
                                            modifier = Modifier.size(20.dp),
                                            tint = RedE31,
                                            contentDescription = null
                                        )

                                        Text(
                                            text = "Seat Information",
                                            color = RedE31,
                                            fontWeight = FontWeight.Bold,
                                            style = MaterialTheme.typography.titleLarge
                                        )
                                    }

                                    HorizontalDivider(
                                        color = RedE31
                                    )


                                    LazyVerticalGrid(
                                        columns = GridCells.Fixed(4),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .heightIn(max = 200.dp),
                                        contentPadding = PaddingValues(8.dp),
                                        verticalArrangement = Arrangement.spacedBy(8.dp),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        items(booking.seats) {
                                            Seat(it.seatCode)
                                        }
                                    }
                                }
                            }
                        }

                        item {
                            Spacer(Modifier.height(20.dp))
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth(0.9f)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(
                                        color = Color.Gray.copy(alpha = 0.1f)
                                    )
                                    .padding(20.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = Color.Transparent
                                )
                            ) {
                                Column(
                                    verticalArrangement = Arrangement.spacedBy(20.dp)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                                    ) {
                                        Icon(
                                            painter = painterResource(R.drawable.ticket),
                                            modifier = Modifier.size(20.dp),
                                            tint = RedE31,
                                            contentDescription = null
                                        )

                                        Text(
                                            text = "Booking Details",
                                            color = RedE31,
                                            fontWeight = FontWeight.Bold,
                                            style = MaterialTheme.typography.titleLarge
                                        )
                                    }

                                    HorizontalDivider(
                                        color = RedE31
                                    )


                                    Column(
                                        verticalArrangement = Arrangement.spacedBy(10.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        DetailsContainer(
                                            title = "BOOKING ID",
                                            value = "${booking._id}"
                                        )
                                        DetailsContainer(
                                            title = "PAYMENT ID",
                                            value = "${booking.paymentId}"
                                        )
                                        DetailsContainer(
                                            title = "STATUS",
                                            value = "${booking.bookingStatus}".uppercase()
                                        )
                                        DetailsContainer(
                                            title = "BOOKING DATE",
                                            value = "${booking.createdAt}"
                                        )

                                    }
                                }
                            }
                        }

                        item {
                            Spacer(Modifier.height(20.dp))
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth(0.9f)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(
                                        color = Color.Gray.copy(alpha = 0.1f)
                                    )
                                    .padding(20.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = Color.Transparent
                                )
                            ) {
                                Column(
                                    verticalArrangement = Arrangement.spacedBy(20.dp)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                                    ) {
                                        Icon(
                                            painter = painterResource(R.drawable.location),
                                            modifier = Modifier.size(20.dp),
                                            tint = RedE31,
                                            contentDescription = null
                                        )

                                        Text(
                                            text = "Address ",
                                            color = RedE31,
                                            fontWeight = FontWeight.Bold,
                                            style = MaterialTheme.typography.titleLarge
                                        )
                                    }

                                    HorizontalDivider(
                                        color = RedE31
                                    )


                                    Column(
                                        verticalArrangement = Arrangement.spacedBy(10.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        DetailsContainer(
                                            title = "Address",
                                            value = "${booking.show.theater.address}"
                                        )
                                        DetailsContainer(
                                            title = "CITY",
                                            value = "${booking.show.theater.city}"
                                        )
                                        DetailsContainer(
                                            title = "STATE",
                                            value = "${booking.show.theater.state}".uppercase()
                                        )
                                        DetailsContainer(
                                            title = "PINCODE",
                                            value = "${booking.show.theater.pincode}"
                                        )

                                    }
                                }
                            }
                        }

                        item {
                            Spacer(Modifier.height(20.dp))
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth(0.9f)
                                    .height(150.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(
                                        brush = Brush.linearGradient(
                                            colors = listOf(
                                                RedE31.copy(alpha = 0.8f),
                                                RedE31,
                                                RedBB0,
                                                RedE31,
                                                RedE31.copy(alpha = 0.8f)
                                            ),
                                            start = Offset(0f, 0f),
                                            end = Offset.Infinite // Diagonal direction
                                        )
                                    ),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = "Rs Total Amount",
                                    style = MaterialTheme.typography.titleLarge
                                )
                                Text(
                                    text = "₹${booking.seats.size * booking.show.ticketCost + (booking.seats.size * booking.show.ticketCost) / 10}",
                                    style = MaterialTheme.typography.displayMedium
                                )
                            }
                        }

                        item {
                            Spacer(Modifier.height(200.dp))
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun DetailsContainer(
    title: String,
    value: String
) {
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(
                Black111
            )
            .fillMaxWidth()
            .padding(10.dp),
        verticalArrangement = Arrangement.spacedBy(7.dp)
    ) {
        Text(
            text = title,
            color = Color.Gray,
            style = MaterialTheme.typography.bodySmall
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.SemiBold
        )

    }
}

@Composable
fun Seat(code: String) {
    Text(
        text = code,
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(RedE31)
            .padding(vertical = 10.dp, horizontal = 17.dp),
        color = Color.White,
        textAlign = TextAlign.Center
    )
}