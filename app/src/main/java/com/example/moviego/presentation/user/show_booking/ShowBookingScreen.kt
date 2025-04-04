package com.example.moviego.presentation.user.show_booking


import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.moviego.R
import com.example.moviego.domain.model.ShowDetails
import com.example.moviego.presentation.admin.components.AccentRed
import com.example.moviego.presentation.admin.components.BorderDark
import com.example.moviego.presentation.admin.components.DarkComponent
import com.example.moviego.presentation.admin.components.ProcessingYellow
import com.example.moviego.presentation.admin.components.TextSecondary
import com.example.moviego.presentation.admin.components.TopBar
import com.example.moviego.presentation.authentication.components.SubmitButton
import com.example.moviego.ui.theme.Black111
import com.example.moviego.ui.theme.Black161
import com.example.moviego.ui.theme.RedE31

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowBookingScreen(
    showDetails: ShowDetails?,
    isLoading: Boolean,
    onEvent: (ShowBookingEvent) -> Unit,
    navController: NavHostController,
    seatSelectionLimit: Int,
    selectedSeats: MutableSet<String>
) {

    var showSeatSelectionSheet by rememberSaveable {
        mutableStateOf(true)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.popBackStack()
                        }
                    ) {
                        Icon(imageVector = Icons.AutoMirrored.Default.ArrowBack, contentDescription = null)
                    }
                },
                title = {
                   if(showDetails != null) {
                      Column {
                          Text(text = showDetails.movie.Title)
                          Text(text = "${showDetails.theater.name} - ${showDetails.screen.screenName} - ${showDetails.date} - ${showDetails.showTime}", style = MaterialTheme.typography.bodyMedium)
                      }
                   }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Black111,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) {
        Column(
            modifier = Modifier
                .padding(
                    top = it.calculateTopPadding(),
                    start = it.calculateStartPadding(
                        layoutDirection = LayoutDirection.Ltr
                    ),
                    end = it.calculateEndPadding(layoutDirection = LayoutDirection.Rtl)
                )
                .padding(horizontal = 10.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
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
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    Row(
                        modifier = Modifier.clickable {
                            showSeatSelectionSheet = true
                        },
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(7.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = null,
                            tint = RedE31
                        )
                        Text(
                            text = "$seatSelectionLimit Tickets",
                            color = RedE31
                        )
                    }
                }

                SeatLegend()

                Spacer(Modifier.height(20.dp))
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
                Spacer(Modifier.height(20.dp))

                showDetails?.let {
                    Row(
                        modifier = Modifier.fillMaxWidth()
                            .weight(1f)
                            .verticalScroll(rememberScrollState())
                    ) {
                        Column(
                            modifier = Modifier.fillMaxHeight(),
                            verticalArrangement = Arrangement.SpaceEvenly
                        ) {
                            for(i in 0..13){

                                Row(
                                    modifier = Modifier.padding(vertical = 5.dp),
                                    verticalAlignment = Alignment.Bottom,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {


                                    Box(
                                        modifier = Modifier
                                            .size(30.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = (i + 65).toChar()
                                                .toString(), // Convert index to alphabet (A, B, C, ...
                                            style = MaterialTheme.typography.titleMedium,
                                            color = RedE31
                                        )
                                    }
                                }


                            }
                        }

                        Column (
                            modifier = Modifier.fillMaxSize()
                                .horizontalScroll(rememberScrollState())
                        ) {

                            showDetails.seats.chunked(20).forEachIndexed{rowIndex, rowSeats ->
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) { 
                                    rowSeats.forEachIndexed{ columnIndex, seat ->
                                        if(columnIndex == 10) {
                                            Spacer(Modifier.width(42.dp))
                                        }
                                        BookingSeat(
                                            seat = seat,
                                            onClick = {
                                                onEvent(ShowBookingEvent.ToggleSeat(seat._id))
                                            },
                                            selected = selectedSeats.contains(seat._id)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
                if(selectedSeats.isNotEmpty()) {
                    if (showDetails != null) {
                        SubmitButton(
                            title = "Pay \u20B9 ${selectedSeats.size * showDetails.ticketCost}",
                            onClick = {

                            },
                            loading = false
                        )
                    }
                }


                if(showSeatSelectionSheet) {
                    ModalBottomSheet(
                        onDismissRequest = {
                            showSeatSelectionSheet = false
                        },
                        containerColor = Black161
                    ) {
                        var seatsToBook by rememberSaveable {
                            mutableIntStateOf(seatSelectionLimit)
                        }
                        Column(
                            modifier = Modifier.fillMaxWidth()
                                .padding(bottom = 20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(text = "How many seats?")
                            val imageSource: Int = when(seatsToBook) {
                                1 -> R.drawable.seats1
                                2 -> R.drawable.seats2
                                3 -> R.drawable.seats3
                                4 -> R.drawable.seats4
                                5 -> R.drawable.seats5
                                else -> R.drawable.seats6
                            }
                            Spacer(modifier = Modifier.height(20.dp))
                            Image(
                                painter = painterResource(imageSource),
                                contentDescription = "Image",
                                modifier = Modifier.size(50.dp)
                            )
                            Spacer(modifier = Modifier.height(20.dp))
                            Row {
                                for(i in 1..6) {
                                    IconButton(
                                        onClick = {
                                            seatsToBook = i
                                        },
                                        colors = IconButtonColors(
                                            containerColor = if (seatsToBook == i) RedE31 else Color.Transparent,
                                            contentColor = Color.White,
                                            disabledContainerColor = Color.Transparent,
                                            disabledContentColor = Color.White
                                        )
                                    ) {
                                        Text(text = i.toString())
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(20.dp))

                            Button(
                                onClick = {
                                    onEvent(ShowBookingEvent.UpdateSeatSelectionLimit(seatsToBook))
                                    showSeatSelectionSheet = false
                                }
                            ) {
                                Text(
                                    text = "Select Seats"
                                )
                            }
                        }
                    }
                }
            }
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
        LegendItem(color = DarkComponent, borderColor = BorderDark, text = "Booked")
        LegendItem(color = RedE31, borderColor = RedE31, text = "Selected")
        LegendItem(color = Black161, borderColor = RedE31, text = "Available")
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