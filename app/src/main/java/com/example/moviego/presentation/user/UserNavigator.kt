package com.example.moviego.presentation.user

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.moviego.presentation.navgraph.user_navgraph.UserNavGraph

@Composable
fun UserNavigator() {
    Scaffold {
        Column(
            modifier = Modifier.fillMaxSize()
                .padding(it)
        ) {
            UserNavGraph()
        }
    }
}