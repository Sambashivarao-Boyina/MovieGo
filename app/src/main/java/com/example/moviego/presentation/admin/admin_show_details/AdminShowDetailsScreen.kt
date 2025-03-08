package com.example.moviego.presentation.admin.admin_show_details

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.unit.dp
import com.example.moviego.domain.model.Seat
import com.example.moviego.domain.model.ShowDetails
import com.example.moviego.presentation.admin.components.SeatComponent

@Composable
fun AdminShowDetailsScreen(
    showDetails:ShowDetails?,
    isLoading: Boolean,
    onEvent:(AdminShowDetailsEvent) -> Unit
) {
    Scaffold {
        if(showDetails != null) {
            Column(
                modifier = Modifier.padding(it).fillMaxSize(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Spacer(Modifier.height(50.dp))
                Canvas(modifier = Modifier.size(800.dp, 10.dp)) {
                    val width = size.width
                    val height = size.height

                    drawPath(
                        path = Path().apply {
                            moveTo(0f, height)
                            quadraticTo(width / 2, -height, width, height) // Control point to create curve
                            lineTo(width, height)
                            lineTo(0f, height)
                            close()
                        },
                        color = Color.Gray
                    )
                }
                Spacer(Modifier.height(50.dp))

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()) // Vertical scrolling
                        .horizontalScroll(rememberScrollState()) // Horizontal scrolling
                ) {
                    // Split seats into rows of 20
                    showDetails.seats.chunked(20).forEach { rowSeats ->
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
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
        } else {
            Text(text = "Loading please wait")
        }
    }
}