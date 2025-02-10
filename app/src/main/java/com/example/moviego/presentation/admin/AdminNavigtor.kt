package com.example.moviego.presentation.admin

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.moviego.presentation.navgraph.admin_navgraph.AdminNavGraph

@Composable
fun AdminNavigator() {
    val navController = rememberNavController()
    Scaffold {
        Column(
            modifier = Modifier.fillMaxSize()
                .padding(it)
        ) {
            AdminNavGraph(navController = navController)
        }
    }
}