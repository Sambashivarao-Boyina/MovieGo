package com.example.moviego.presentation.user.payment_confirmation

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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import com.example.moviego.domain.model.Booking
import com.example.moviego.presentation.admin.components.TopBar
import com.example.moviego.presentation.authentication.components.SubmitButton
import com.example.moviego.ui.theme.Black111
import com.example.moviego.ui.theme.Black161

@Composable
fun UserPaymentConfirmationScreen(
    navController: NavHostController,
    booking: Booking?,
    isLoading: Boolean,
    onEvent: (UserPaymentConfirmationEvent) -> Unit
) {
    var showCancelConfirmDialog by rememberSaveable {
        mutableStateOf(false)
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
                        Icon(imageVector = Icons.AutoMirrored.Default.ArrowBack, contentDescription = "back")
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
            if(isLoading) {
                CircularProgressIndicator()
            } else {
                booking?.let {
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth()
                            .weight(1f)
                    ) {

                    }
                    Row(
                        modifier = Modifier.fillMaxWidth()
                            .padding(horizontal = 20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            modifier = Modifier.weight(0.5f)
                        ) {
                            Text(text = "Total", color = Color.Gray)
                            Text(text = "₹ ${booking.seats.size * booking.show.ticketCost}", style = MaterialTheme.typography.titleLarge)
                        }
                        Row(
                            modifier = Modifier.weight(0.5f)
                        ) {
                            SubmitButton(
                                onClick = {

                                },
                                title = "Continue",
                                loading = false

                            )
                        }
                    }
                } ?: Text(text = "Something went wrong")
            }
        }

        if(showCancelConfirmDialog) {
            Dialog(
                onDismissRequest = {
                    showCancelConfirmDialog = false
                },

            ) {
                Column(
                    modifier = Modifier.clip(RoundedCornerShape(20.dp)).background(Black161).padding(horizontal = 10.dp, vertical = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(text = "Cancel the Booking", style = MaterialTheme.typography.titleLarge)
                    Text(text = "Are you sure do you want to cancel the Booking?", style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(30.dp))
                    Row (
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ){
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
                                onEvent(UserPaymentConfirmationEvent.CancelBooking)
                                navController.popBackStack()
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