package com.example.moviego.presentation.admin.admin_theater_details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.moviego.R
import com.example.moviego.domain.model.TheaterDetails
import com.example.moviego.presentation.components.ScreenCard

@Composable
fun AdminTheaterDetailsScreen(
    theaterDetails: TheaterDetails?,
    isLoading: Boolean,
    navController: NavHostController
) {
    if(isLoading) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator()
        }
    }

    if(!isLoading && theaterDetails == null) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Data is Empty"
            )
        }
    }

    theaterDetails?.let {
        LazyColumn(
            modifier = Modifier.fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),

        ) {
            item {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(theaterDetails.image)
                        .placeholder(R.drawable.placeholder)
                        .error(R.drawable.placeholder)
                        .build(),
                    contentDescription = theaterDetails.name,
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .height(300.dp)
                        .fillMaxWidth(),
                    contentScale = ContentScale.Crop,
                )
            }

            item {
                Text(text = theaterDetails.name, style = MaterialTheme.typography.displaySmall, color = Color.Red)
            }

            item {
                Text(
                    text = "${theaterDetails.address}, ${theaterDetails.city}, ${theaterDetails.state} - ${theaterDetails.pincode}",
                )
                Text(
                    text = "Contact : ${theaterDetails.contactNumber}",
                )
            }

            item {
                Text(text = "Screens", style = MaterialTheme.typography.titleLarge)
            }

            items(theaterDetails.screens) { screen ->
                ScreenCard(screen)
            }
        }
    }
}