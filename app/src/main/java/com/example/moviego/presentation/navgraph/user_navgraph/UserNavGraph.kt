package com.example.moviego.presentation.navgraph.user_navgraph

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.moviego.presentation.navgraph.Route

@Composable
fun UserNavGraph() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Route.UserHomeRoute.route
    ){
        composable(route = Route.UserHomeRoute.route){
            Text(text ="User Home")
        }
    }
}