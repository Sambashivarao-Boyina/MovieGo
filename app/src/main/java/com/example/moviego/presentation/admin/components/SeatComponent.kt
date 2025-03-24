package com.example.moviego.presentation.admin.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.moviego.domain.model.Seat
import com.example.moviego.ui.theme.MovieGoTheme

val DarkBackground = Color(0xFF121212)
val DarkSurface = Color(0xFF1E1E1E)
val DarkComponent = Color(0xFF2A2A2A)
val AccentRed = Color(0xFFE50914)
val ProcessingYellow = Color(0xFFFF9900)
val TextPrimary = Color.White
val TextSecondary = Color(0xFFCCCCCC)
val BorderDark = Color(0xFF444444)

@Composable
fun SeatComponent(
    seat: Seat
) {
    Row(
        modifier = Modifier.padding(5.dp),
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        val (backgroundColor, borderColor, textColor) = if (seat.status ==  "Available") {
            Triple(DarkComponent, BorderDark, TextSecondary)
        } else if(seat.status == "Booked") {
            Triple(AccentRed, AccentRed, TextPrimary)
        } else {
            Triple(ProcessingYellow, ProcessingYellow, Color.Black)
        }

        Box(
            modifier = Modifier
                .size(30.dp)
                .background(backgroundColor, RoundedCornerShape(4.dp))
                .border(1.dp, borderColor, RoundedCornerShape(4.dp))
                .clickable {  },
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

