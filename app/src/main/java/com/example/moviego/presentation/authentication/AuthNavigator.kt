package com.example.moviego.presentation.authentication

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.moviego.presentation.navgraph.auth_navgraph.AuthNavGraph

@Composable
fun AuthNavigator() {
    Scaffold {
        LazyColumn(
            modifier = Modifier.fillMaxSize()
                .padding(it)
        ) {
            item {
                AuthNavGraph()
            }
        }
    }
}