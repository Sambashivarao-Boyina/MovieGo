package com.example.moviego.presentation.user.show_booking

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.moviego.domain.model.Seat
import com.example.moviego.ui.theme.Black161
import com.example.moviego.ui.theme.RedE31

val DarkComponent = Color(0xFF2A2A2A)
val BorderDark = Color(0xFF444444)

@Composable
fun BookingSeat(
    seat: Seat,
    onClick:()->Unit,
    selected: Boolean
) {
    Row(
        modifier = Modifier.padding(5.dp),
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        val (backgroundColor, borderColor, textColor) = if(selected) {
            Triple(RedE31, RedE31, Color.White)
        } else if(seat.status == "Booked") {
            Triple(DarkComponent, BorderDark, Color.Gray)
        } else {
            Triple(RedE31, Black161, Color.Gray)
        }

        Box(
            modifier = Modifier
                .size(30.dp)
                .background(backgroundColor, RoundedCornerShape(4.dp))
                .border(1.dp, borderColor, RoundedCornerShape(4.dp))
                .clickable {
                    if(seat.status == "Available") {
                        onClick()
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = seat.seatCode.substring(1), // Show only the number part
                color = textColor,
                fontSize = 12.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}