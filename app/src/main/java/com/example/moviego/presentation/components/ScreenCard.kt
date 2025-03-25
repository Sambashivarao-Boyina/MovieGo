package com.example.moviego.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.moviego.R
import com.example.moviego.domain.model.Screen
import com.example.moviego.presentation.admin.theater_details.AdminTheaterDetailsEvent
import com.example.moviego.ui.theme.Black1C1
import com.example.moviego.ui.theme.RedE31

@Composable
fun ScreenCard(screen: Screen, onEvent: (AdminTheaterDetailsEvent)-> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
                .background(Black1C1),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {

            Row(
                modifier = Modifier.fillMaxWidth().padding(10.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = screen.screenName, modifier = Modifier.weight(0.8f).padding(bottom = 5.dp), textAlign = TextAlign.Start, style = MaterialTheme.typography.titleLarge, color = RedE31)
                IconButton(
                    onClick = {
                        onEvent(AdminTheaterDetailsEvent.SetEditScreenId(screenId = screen._id))
                        onEvent(AdminTheaterDetailsEvent.UpdateEditScreenName(screenName = screen.screenName))
                        onEvent(AdminTheaterDetailsEvent.UpdateEditScreenType(screenType = screen.screenType))
                        onEvent(AdminTheaterDetailsEvent.UpdateEditSoundType(soundType = screen.soundType))
                        onEvent(AdminTheaterDetailsEvent.UpdateShowEditScreen(showScreen = true))
                    }
                ) {
                    Icon(imageVector = Icons.Default.Edit, tint = RedE31, contentDescription = "Edit")
                }
            }
            HorizontalDivider()
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Row(
                    modifier = Modifier.weight(0.5f).padding(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.Start
                ) {
                    Icon(painter = painterResource(R.drawable.movie), contentDescription = null, modifier = Modifier.size(20.dp))
                    Spacer(Modifier.width(10.dp))
                    Text(text = screen.screenType)
                }
                Row(
                    modifier = Modifier.weight(0.5f).padding(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.Start
                ) {
                    Icon(painter = painterResource(R.drawable.headphones), contentDescription = null, modifier = Modifier.size(20.dp))
                    Spacer(Modifier.width(10.dp))
                    Text(text = screen.soundType)
                }
            }


        }



    }
}