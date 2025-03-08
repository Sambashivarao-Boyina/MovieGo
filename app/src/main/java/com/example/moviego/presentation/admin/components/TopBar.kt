package com.example.moviego.presentation.admin.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.moviego.ui.theme.Black111

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    title: String,
    navigationBox :  @Composable () -> Unit = {},
    actions:   @Composable () -> Unit = {}
) {
    TopAppBar(
        navigationIcon = {
            navigationBox()
        },
        title = {
            Text(text = title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold)
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Black111,
            titleContentColor = Color.White,
            navigationIconContentColor = Color.White
        ),
        actions = {
            actions()
        }
    )

}