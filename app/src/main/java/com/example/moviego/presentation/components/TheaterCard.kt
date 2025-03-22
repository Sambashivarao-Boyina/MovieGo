package com.example.moviego.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.moviego.R
import com.example.moviego.domain.model.TheaterDetails
import com.example.moviego.ui.theme.Black161
import com.example.moviego.ui.theme.RedE31


@Composable
fun TheaterCard(theater: TheaterDetails, onClick:()->Unit, onEditClick:()->Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Black161), // Dark background
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Box {
            Column {
                // Theater Image

                Box{
                    AsyncImage(
                        model = theater.image,
                        contentDescription = "Theater Image",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                            .clickable { onClick() }
                            .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)),
                        contentScale = ContentScale.Crop
                    )

                    Box(
                        modifier = Modifier.align(Alignment.BottomStart)
                            .padding(10.dp)
                    ){
                        Text(
                            text = theater.name,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }

                }


                // Theater Info
                Column(
                    modifier = Modifier
                        .clickable { onClick() }
                        .fillMaxWidth()
                        .padding(12.dp)
                ) {

                    TheaterDetailRow(Icons.Outlined.Place, theater.address)
                    TheaterDetailRow(Icons.Outlined.Person, "${theater.city}, ${theater.state} - ${theater.pincode}")
                    TheaterDetailRow(Icons.Outlined.Phone, theater.contactNumber)

                }
            }

            // Screens Count Badge
            Box(
                modifier = Modifier
                    .padding(8.dp)
                    .align(Alignment.TopEnd)
                    .background(Color.Red, shape = RoundedCornerShape(16.dp))
                    .padding(horizontal = 10.dp, vertical = 4.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(R.drawable.show),
                        contentDescription = "Screens",
                        tint = Color.White,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${theater.screens.size} Screens",
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
            ){
                IconButton(
                    onClick = {
                        onEditClick()
                    }
                ) {
                    Row {
                        Icon(
                            imageVector = Icons.Outlined.Edit,
                            contentDescription = "Edit Theater",
                            tint = Color.Red
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TheaterDetailRow(icon: ImageVector, text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 2.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = RedE31,
            modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = text, fontSize = 14.sp, color = Color(0xFFDDDDDD))
    }
}