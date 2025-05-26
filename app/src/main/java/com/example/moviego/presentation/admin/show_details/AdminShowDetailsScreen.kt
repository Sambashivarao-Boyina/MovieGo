package com.example.moviego.presentation.admin.show_details

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.zIndex
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.moviego.R
import com.example.moviego.domain.model.Movie
import com.example.moviego.domain.model.Screen
import com.example.moviego.domain.model.ShowDetails
import com.example.moviego.domain.model.Theater
import com.example.moviego.presentation.admin.components.AccentRed
import com.example.moviego.presentation.admin.components.BorderDark
import com.example.moviego.presentation.admin.components.DarkComponent
import com.example.moviego.presentation.admin.components.DarkSurface
import com.example.moviego.presentation.admin.components.ProcessingYellow
import com.example.moviego.presentation.admin.components.SeatComponent
import com.example.moviego.presentation.admin.components.TextPrimary
import com.example.moviego.presentation.admin.components.TextSecondary
import com.example.moviego.presentation.admin.components.TopBar
import com.example.moviego.ui.theme.Black111
import com.example.moviego.ui.theme.RedE31
import com.mapbox.maps.Style

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminShowDetailsScreen(
    showDetails: ShowDetails?,
    isLoading: Boolean,
    onEvent: (AdminShowDetailsEvent) -> Unit,
    navController: NavHostController,
    updatingShowStatus: Boolean
) {
    var expanded by remember {
        mutableStateOf(false)
    }
    Scaffold(
        topBar = {
            TopBar(
                title = "Show Details",
                navigationBox = {
                    IconButton(
                        onClick = {
                            navController.popBackStack()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = null
                        )
                    }
                },
                actions = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .zIndex(1f), // Ensures it's above Map
                            contentAlignment = Alignment.BottomStart
                        ) {

                            IconButton(
                                onClick = { expanded = true },
                                modifier = Modifier.align(Alignment.BottomStart)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.MoreVert,
                                    contentDescription = "Menu",
                                )
                            }

                            DropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false }
                            ) {

                                if (showDetails != null) {
                                    if (showDetails.bookingStatus == "Open") {
                                        DropdownMenuItem(
                                            text = { Text("Close Bookings") },
                                            onClick = {
                                                expanded = false
                                                onEvent(AdminShowDetailsEvent.CloseShow)
                                            }
                                        )
                                    } else {
                                        DropdownMenuItem(
                                            text = { Text("Open Bookings") },
                                            onClick = {
                                                expanded = false
                                                onEvent(AdminShowDetailsEvent.OpenShow)
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            )

        }
    ) {
        if (isLoading) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator()
            }
        } else {
            if (showDetails != null) {
                LazyColumn(
                    modifier = Modifier
                        .padding(it)
                        .padding(horizontal = 10.dp)
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    item {
                        MovieInfoHeader(
                            movie = showDetails.movie,
                            ticketCost = showDetails.ticketCost,
                            status = showDetails.bookingStatus
                        )
                    }

                    // Theater & Show Info
                    item {
                        TheaterShowInfo(
                            theater = showDetails.theater,
                            screen = showDetails.screen,
                            date = showDetails.date,
                            showTime = showDetails.showTime
                        )
                    }

                    // Booking Status
                    item {
                        BookingStatus(
                            bookedCount = showDetails.bookedSeatsCount,
                            totalSeats = showDetails.seats.size
                        )
                    }

                    // Seat Status Legend
                    item {
                        SeatLegend()
                    }



                    item {
                        Spacer(Modifier.height(50.dp))
                        Canvas(modifier = Modifier.size(800.dp, 10.dp)) {
                            val width = size.width
                            val height = size.height

                            drawPath(
                                path = Path().apply {
                                    moveTo(0f, height)
                                    quadraticTo(
                                        width / 2,
                                        -height,
                                        width,
                                        height
                                    ) // Control point to create curve
                                    lineTo(width, height)
                                    lineTo(0f, height)
                                    close()
                                },
                                color = RedE31
                            )
                        }
                        Spacer(Modifier.height(50.dp))

                        Column(
                            modifier = Modifier
                                .fillMaxSize() // Vertical scrolling
                                .horizontalScroll(rememberScrollState()) // Horizontal scrolling
                        ) {
                            // Split seats into rows of 20
                            showDetails.seats.chunked(20).forEachIndexed { rowIndex, rowSeats ->
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    // Display the alphabet for the row (A, B, C, etc.)
                                    Text(
                                        text = (rowIndex + 65).toChar()
                                            .toString(), // Convert index to alphabet (A, B, C, ...)
                                        modifier = Modifier.width(32.dp),
                                        style = MaterialTheme.typography.titleMedium,
                                        color = RedE31
                                    )

                                    rowSeats.forEachIndexed { columnIndex, seat ->
                                        if (columnIndex == 10) {
                                            Spacer(modifier = Modifier.width(42.dp)) // Gap between column 10 and 11
                                        }
                                        SeatComponent(seat) // Call your custom Seat component
                                    }
                                }
                            }
                        }
                    }

                    item {
                        Spacer(Modifier.height(200.dp))
                    }

                }
            } else {
                Text(text = "Loading please wait")
            }
        }


        if (updatingShowStatus) {
            Dialog(
                onDismissRequest = {

                },

                ) {
                Column(
                    modifier = Modifier
                        .height(150.dp)
                        .width(300.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(Black111),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator()
                    Spacer(Modifier.height(10.dp))
                    Text(text = "Updating Status Please wait")
                }
            }
        }
    }
}


@Composable
fun MovieInfoHeader(movie: Movie, ticketCost: Int, status: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(8.dp)
            .border(
                width = 1.dp,
                color = BorderDark,
                shape = RoundedCornerShape(8.dp)
            ),
        colors = CardDefaults.cardColors(
            containerColor = DarkSurface
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Movie Poster
            AsyncImage(
                model = movie.Poster,
                contentDescription = "${movie.Title} Poster",
                modifier = Modifier
                    .width(100.dp)
                    .height(150.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .border(
                        width = 1.dp,
                        color = AccentRed,
                        shape = RoundedCornerShape(8.dp)
                    ),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(16.dp))

            // Movie Details
            Column(modifier = Modifier.weight(1f)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    Text(
                        text = status.uppercase(), modifier = Modifier
                            .clip(RoundedCornerShape(30.dp))
                            .background(
                                RedE31
                            )
                            .padding(
                                horizontal = 10.dp,
                                vertical = 5.dp),
                        fontWeight = FontWeight.Bold
                    )
                }
                Text(
                    text = movie.Title,
                    color = TextPrimary,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Duration: ${movie.Runtime} minutes",
                    color = TextSecondary,
                    fontSize = 14.sp
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Language: ${movie.Language}",
                    color = TextSecondary,
                    fontSize = 14.sp
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Ticket Cost: ₹$ticketCost",
                    color = TextSecondary,
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Composable
fun TheaterShowInfo(theater: Theater, screen: Screen, date: String, showTime: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(8.dp)
            .border(
                width = 1.dp,
                color = BorderDark,
                shape = RoundedCornerShape(8.dp)
            ),
        colors = CardDefaults.cardColors(
            containerColor = DarkSurface
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                // Theater Info
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = theater.name,
                        color = TextPrimary,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = theater.address,
                        color = TextSecondary,
                        fontSize = 14.sp
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = "${theater.city}, ${theater.state} - ${theater.pincode}",
                        color = TextSecondary,
                        fontSize = 14.sp
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Screen: ${screen.screenName} (${screen.screenType}, ${screen.soundType})",
                        color = TextSecondary,
                        fontSize = 14.sp
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                // Show Timing
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "Date: $date",
                        color = TextSecondary,
                        fontSize = 14.sp
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Time: $showTime",
                        color = TextSecondary,
                        fontSize = 14.sp
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Contact: ${theater.contactNumber}",
                        color = TextSecondary,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}

@Composable
fun BookingStatus(bookedCount: Int, totalSeats: Int) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(4.dp)
            .border(
                width = 2.dp,
                color = AccentRed,
                shape = RoundedCornerShape(8.dp)
            ),
        colors = CardDefaults.cardColors(
            containerColor = DarkComponent
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Booking Status: $bookedCount seats booked out of $totalSeats seats",
                color = TextPrimary,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun SeatLegend() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        LegendItem(color = DarkComponent, borderColor = BorderDark, text = "Available")
        LegendItem(color = AccentRed, borderColor = AccentRed, text = "Booked")
        LegendItem(color = ProcessingYellow, borderColor = ProcessingYellow, text = "Processing")
    }
}

@Composable
fun LegendItem(color: Color, borderColor: Color, text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(20.dp)
                .background(color, RoundedCornerShape(4.dp))
                .border(1.dp, borderColor, RoundedCornerShape(4.dp))
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(text = text, color = TextSecondary, fontSize = 14.sp)
    }
}
