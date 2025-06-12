package com.example.moviego.presentation.user.bookings

import androidx.annotation.DrawableRes
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
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
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.moviego.R
import com.example.moviego.domain.model.BookingDetails
import com.example.moviego.presentation.admin.shows.AdminShowsEvent
import com.example.moviego.presentation.components.shimmerEffect
import com.example.moviego.presentation.navgraph.Route
import com.example.moviego.ui.theme.Black111
import com.example.moviego.ui.theme.Black161
import com.example.moviego.ui.theme.Black1C1
import com.example.moviego.ui.theme.RedBB0
import com.example.moviego.ui.theme.RedE31
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import java.util.Locale

@Composable
fun UserBookingsScreen(
    isLoading: Boolean,
    bookingDetails: List<BookingDetails>,
    onEvent:(UserBookingsEvent) -> Unit,
    navController: NavHostController
) {
    val swipeRefreshState = rememberSwipeRefreshState(
        isRefreshing = isLoading,
    )
    Scaffold {
        if(isLoading) {
            Column(
                modifier = Modifier.padding(it)
            ) {
                for(i in 1..6) {
                    Box(
                        modifier = Modifier.padding(10.dp).height(200.dp).fillMaxWidth().clip(RoundedCornerShape(20.dp)).shimmerEffect()
                    )
                }
            }
        } else {
            SwipeRefresh(
                state = swipeRefreshState,
                onRefresh = {
                    onEvent(UserBookingsEvent.ReloadData)
                },
                modifier = Modifier.fillMaxSize()
            ){
                LazyColumn(
                    modifier = Modifier.padding(it).padding(horizontal = 10.dp).fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(13.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    items(bookingDetails) {
                        BookingCard(it, onClick = {
                            navController.navigate(Route.UserBookingDetails.passBookingId(it._id))
                        })
                    }
                }
            }

        }
    }
}

@Composable
fun BookingCard(bookingDetails: BookingDetails, onClick:()->Unit) {
    Card(
        elevation = CardDefaults.elevatedCardElevation(8.dp),
        colors = CardDefaults.cardColors().copy(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Black161)
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
                        .data(bookingDetails.show.movie.Poster)
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
                        Text(text = bookingDetails.show.movie.Title, style = MaterialTheme.typography.titleLarge)
                        Spacer(modifier = Modifier.height(6.dp))
                        Row {
                            Icon(
                                painter = painterResource(R.drawable.location),
                                tint = RedE31,
                                contentDescription = null,
                                modifier = Modifier.size(20.dp)
                            )
                            Text(
                                text = "${bookingDetails.show.theater.name} ${bookingDetails.show.theater.city}",
                                color = Color.Gray
                            )
                        }
                    }

                    Row(
                        modifier = Modifier.align(Alignment.TopEnd)
                    ) {
                        BookingStatus(status = bookingDetails.bookingStatus)
                    }

                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth().height(90.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                DataBlock(
                    icon = R.drawable.calender,
                    title = "Date",
                    data = bookingDetails.show.date
                )
                VerticalDivider(Modifier.fillMaxHeight(0.7f))
                DataBlock(
                    icon = R.drawable.time,
                    title = "Time",
                    data = bookingDetails.show.showTime
                )
                VerticalDivider(Modifier.fillMaxHeight(0.7f))
                DataBlock(
                    icon = R.drawable.screen,
                    title = "Screen",
                    data = bookingDetails.show.screen.screenName
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
                    bookingDetails.seats.map {
                        Text("${it.seatCode}", color = RedE31, fontWeight = FontWeight.Bold)
                    }
                }

                Text(
                    text = "₹${bookingDetails.seats.size * bookingDetails.show.ticketCost + (bookingDetails.seats.size * bookingDetails.show.ticketCost) / 10 }",
                    color = RedE31,
                    fontWeight = FontWeight.Bold
                )
            }

        }
    }
}


@Composable
fun DataBlock(
    icon: Int,
    title: String,
    data: String
) {
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(Color.White.copy(alpha = 0.04f))
            .padding(horizontal = 10.dp, vertical = 5.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Icon(painter = painterResource(icon), modifier = Modifier.size(20.dp), tint = RedE31, contentDescription = null)
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
            .background(color = RedE31)
            .padding(horizontal = 10.dp, vertical = 5.dp)
    ) {
        Text(text = status.uppercase(Locale.ROOT), color = Color.White, modifier = Modifier.background(RedE31), fontWeight = FontWeight.Bold)
    }
}