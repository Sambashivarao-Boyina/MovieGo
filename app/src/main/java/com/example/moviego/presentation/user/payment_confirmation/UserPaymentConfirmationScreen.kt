package com.example.moviego.presentation.user.payment_confirmation

import android.annotation.SuppressLint
import android.app.Activity
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavHostController
import com.example.moviego.domain.model.Booking
import com.example.moviego.presentation.admin.components.TopBar
import com.example.moviego.presentation.authentication.components.SubmitButton
import com.example.moviego.presentation.navgraph.Route
import com.example.moviego.ui.theme.Black161
import com.example.moviego.ui.theme.RedE31

@SuppressLint("DefaultLocale")
@Composable
fun UserPaymentConfirmationScreen(
    navController: NavHostController,
    booking: Booking?,
    isLoading: Boolean,
    onEvent: (UserPaymentConfirmationEvent) -> Unit,
    cancelingBooking: Boolean,
    navigateBack: Boolean,
    ticketsPrice: Float,
    convenienceFees: Float,
    totalPayment: Float,
    paymentState: PaymentState
) {
    var showCancelConfirmDialog by rememberSaveable {
        mutableStateOf(false)
    }

    val context = LocalContext.current

    LaunchedEffect(key1 = navigateBack) {
        Log.d("changed", "changed")
        if (navigateBack) {
            navController.popBackStack()
        }
    }

    LaunchedEffect(key1 = paymentState) {
        when(paymentState) {
            is PaymentState.Success -> {
                navController.popBackStack(route = Route.UserHomeRoute.route, inclusive = false)
                onEvent(UserPaymentConfirmationEvent.ResetConfirmation)
            }
            is PaymentState.Error -> {
                onEvent(UserPaymentConfirmationEvent.ResetConfirmation)
                navController.popBackStack(route = Route.UserMovieShowBooking.route, inclusive = false)
            }
            else -> {

            }
        }
    }


    BackHandler(enabled = true) {
        showCancelConfirmDialog = true
    }

    Scaffold(
        topBar = {
            TopBar(
                title = "Confirm Booking",
                navigationBox = {
                    IconButton(
                        onClick = {
                            showCancelConfirmDialog = true
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
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        item {
                            Card(
                                modifier = Modifier
                                    .padding(10.dp)
                                    .fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors().copy(
                                    containerColor = Black161
                                ),
                                elevation = CardDefaults.cardElevation(
                                    defaultElevation = 4.dp
                                )
                            ) {
                                Column(
                                    modifier = Modifier
                                        .padding(
                                            vertical = 20.dp,
                                            horizontal = 15.dp
                                        )
                                        .fillMaxWidth()
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.Top
                                    ) {
                                        Column {
                                            Text(
                                                text = booking.show.movie.Title,
                                                style = MaterialTheme.typography.titleLarge
                                            )
                                            Text(
                                                text = "${booking.show.date} | ${booking.show.showTime}",
                                                style = MaterialTheme.typography.titleSmall
                                            )
                                        }
                                        Column(
                                            horizontalAlignment = Alignment.End
                                        ) {
                                            Text(
                                                text = "${booking.seats.size}",
                                                style = MaterialTheme.typography.titleLarge,

                                                )
                                            Text(text = "Box Office", color = RedE31)
                                        }

                                    }
                                    Text(
                                        text = "${booking.show.movie.Language}",
                                        style = MaterialTheme.typography.titleSmall,
                                        color = Color.Gray
                                    )
                                    Text(
                                        text = "${booking.show.theater.name} ${booking.show.screen.screenType} ${booking.show.screen.soundType} ${booking.show.theater.city} (${booking.show.screen.screenName}) ",
                                        style = MaterialTheme.typography.titleSmall,
                                        color = Color.Gray
                                    )
                                }
                            }
                        }

                        item {
                            Card(
                                modifier = Modifier
                                    .padding(10.dp)
                                    .fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors().copy(
                                    containerColor = Black161
                                ),
                                elevation = CardDefaults.cardElevation(
                                    defaultElevation = 4.dp
                                )
                            ) {
                                Column(
                                    modifier = Modifier
                                        .padding(
                                            vertical = 20.dp,
                                            horizontal = 15.dp
                                        )
                                        .fillMaxWidth(),
                                    verticalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            text = "Ticket(s) price",
                                            color = Color.Gray
                                        )
                                        Text(
                                            text = "₹${String.format("%.2f", ticketsPrice)}"
                                        )
                                    }
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            text = "Convenience fees",
                                            color = Color.Gray
                                        )
                                        Text(
                                            text = "₹${String.format("%.2f", convenienceFees)}"
                                        )
                                    }

                                    HorizontalDivider()

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            text = "Order Total",
                                            style = MaterialTheme.typography.titleLarge
                                        )
                                        Text(
                                            text = "₹${String.format("%.2f", totalPayment)}",
                                            color = RedE31,
                                            style = MaterialTheme.typography.titleLarge
                                        )
                                    }

                                }
                            }
                        }

                        item {
                            Card(
                                modifier = Modifier
                                    .padding(10.dp)
                                    .fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors().copy(
                                    containerColor = Black161
                                ),
                                elevation = CardDefaults.cardElevation(
                                    defaultElevation = 4.dp
                                )
                            ) {
                                Column(
                                    modifier = Modifier
                                        .padding(
                                            vertical = 20.dp,
                                            horizontal = 15.dp
                                        )
                                        .fillMaxWidth(),
                                    verticalArrangement = Arrangement.spacedBy(5.dp)
                                ) {
                                    Text(text = "Your Details", style = MaterialTheme.typography.titleLarge)
                                    Text(text = "${booking.user.email}")
                                    Text(text = "${booking.user.phone}")
                                }
                            }
                        }
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            modifier = Modifier.weight(0.5f)
                        ) {
                            Text(text = "Total", color = Color.Gray)
                            Text(
                                text = "₹${String.format("%.2f", totalPayment)}",
                                style = MaterialTheme.typography.titleLarge
                            )
                        }
                        Row(
                            modifier = Modifier.weight(0.5f)
                        ) {
                            SubmitButton(
                                onClick = {
                                    onEvent(UserPaymentConfirmationEvent.StartPayment(context as Activity))
                                },
                                title = "Continue",
                                loading = false

                            )
                        }
                    }
                } ?: Text(text = "Something went wrong")
            }
        }

        if (cancelingBooking) {
            Dialog(
                onDismissRequest = {

                },
                properties = DialogProperties(
                    dismissOnClickOutside = false,
                    dismissOnBackPress = false,
                    usePlatformDefaultWidth = true
                )
            ) {
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(Black161)
                        .padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    CircularProgressIndicator()
                    Text(
                        text = "We are canceling your booking please wait",
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
        if (showCancelConfirmDialog) {
            Dialog(
                onDismissRequest = {
                    showCancelConfirmDialog = false
                },

                ) {
                Column(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(Black161)
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "Cancel the Booking",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.Gray
                    )
                    Text(
                        text = "Are you sure do you want to cancel the Booking?",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(30.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Button(
                            onClick = {
                                showCancelConfirmDialog = false
                            }
                        ) {
                            Text(text = "Cancel")
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Button(
                            onClick = {
                                showCancelConfirmDialog = false
                                onEvent(UserPaymentConfirmationEvent.CancelBooking)

                            }
                        ) {
                            Text(text = "Confirm")
                        }
                    }
                }
            }
        }
    }
}