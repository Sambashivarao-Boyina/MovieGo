package com.example.moviego.presentation.authentication.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.moviego.R
import com.example.moviego.ui.theme.Black1C1
import com.example.moviego.ui.theme.MovieGoTheme
import com.example.moviego.ui.theme.RedBB0
import com.example.moviego.ui.theme.RedE31

@Composable
fun PersonCard(
    image: Int,
    title: String,
    description: String,
    onClick:()->Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.height(200.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
                .fillMaxHeight()
                .background(Black1C1),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Image(painter = painterResource(image), contentDescription = null , modifier = Modifier.fillMaxHeight())

            Column(
                modifier = Modifier.weight(0.5f)
                    .padding(20.dp)

            ) {
                Text(text = title, style = MaterialTheme.typography.headlineLarge, color = RedE31)
                Text(text = description)
            }
        }
    }
}

