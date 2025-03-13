package com.example.moviego.presentation.admin.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.moviego.R
import com.example.moviego.domain.model.Show
import com.example.moviego.ui.theme.Black111
import com.example.moviego.ui.theme.Black161
import com.example.moviego.ui.theme.RedBB0
import com.example.moviego.ui.theme.RedE31

@Composable
fun ShowCard(
    show: Show,
    onClick:()-> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(240.dp)
            .clickable { },
        colors = CardDefaults.cardColors().copy(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(show.movie.posterUrl)
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.placeholder)
                    .build(),
                contentDescription = null,
                modifier = Modifier
                    .weight(0.4f)
                    .fillMaxHeight(),
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier
                    .weight(0.6f)
                    .padding(20.dp)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Column {
                    Text(
                        text = show.movie.title,
                        style = MaterialTheme.typography.titleLarge
                    )
                    Row(
                        modifier = Modifier.height(20.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            text = show.movie.language,
                            color = Color.Gray
                        )
                        VerticalDivider(color = Color.White)
                        Text(
                            text = "${show.movie.duration} min",
                            color = Color.Gray
                        )
                    }
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(9.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.location),
                        tint = RedE31,
                        contentDescription = null,
                        modifier = Modifier.size(17.dp)
                    )
                    Text(text = show.theater.name)
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(9.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.calender),
                        tint = RedE31,
                        contentDescription = null,
                        modifier = Modifier.size(17.dp)
                    )
                    Text(text = show.date)
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.clock_outline),
                        tint = RedE31,
                        contentDescription = null,
                        modifier = Modifier.size(17.dp)
                    )
                    Text(text = show.showTime)
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.movie),
                        tint = RedE31,
                        contentDescription = null,
                        modifier = Modifier.size(17.dp)
                    )
                    Text(text ="${show.screen.screenName} (${show.screen.screenType})")
                }
                Spacer(modifier = Modifier.height(5.dp))
                Row(
                    modifier = Modifier.fillMaxWidth().clip(
                        RoundedCornerShape(10.dp)
                    ).background(Black161).padding(5.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Column(
                        modifier = Modifier.weight(0.45f),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "\u20B9 ${show.ticketCost}", color = RedE31)
                        Text(text = "Ticket Cost",style = MaterialTheme.typography.labelLarge, color = Color.Gray)
                    }
                    VerticalDivider(color = Color.Gray)
                    Column(
                        modifier = Modifier.weight(0.45f),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "${show.bookedSeatsCount}", color = RedE31)
                        Text(text = "Booked Seats", style = MaterialTheme.typography.labelLarge, color = Color.Gray)
                    }
                }
            }
        }

    }
}