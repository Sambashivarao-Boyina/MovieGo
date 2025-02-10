package com.example.moviego.presentation.navgraph.admin_navgraph

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.moviego.presentation.navgraph.Route

@Composable
fun AdminNavGraph(
    navController: NavHostController
) {

    NavHost(
        navController = navController,
        startDestination = Route.AdminShows.route
    ){
        composable(route = Route.AdminShows.route){
            Text(text ="Admin Home")
        }
    }
}