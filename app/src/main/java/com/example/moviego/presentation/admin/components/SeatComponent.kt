package com.example.moviego.presentation.admin.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.moviego.domain.model.Seat
import com.example.moviego.ui.theme.MovieGoTheme

@Composable
fun SeatComponent(
    seat: Seat
) {
    Row(
        modifier = Modifier.padding(5.dp),
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Box(
           modifier = Modifier.height(30.dp)
               .width(10.dp)
               .clip(RoundedCornerShape(6.dp))
               .background(Color.Gray)
        )
        Box(
            modifier = Modifier.height(35.dp)
                .width(20.dp)
                .clip(RoundedCornerShape(5.dp))
                .background(Color.Gray),
            contentAlignment = Alignment.Center
        ) {
            Text( text = seat.seatCode, fontSize = 9.sp)
        }
        Box(
            modifier = Modifier.height(30.dp)
                .width(10.dp)
                .clip(RoundedCornerShape(5.dp))
                .background(Color.Gray)
        )
    }
}

//@Preview(showBackground = true)
//@Composable
//fun SeatPreview() {
//    MovieGoTheme {
//        SeatComponent()
//    }
//}
