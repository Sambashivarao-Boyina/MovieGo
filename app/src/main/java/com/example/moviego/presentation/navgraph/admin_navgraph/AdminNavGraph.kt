package com.example.moviego.presentation.navgraph.admin_navgraph

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.moviego.presentation.admin.admin_shows.AdminShowsScreen
import com.example.moviego.presentation.admin.admin_shows.AdminShowsViewModel
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
            val adminShowsViewModel: AdminShowsViewModel = hiltViewModel()
            AdminShowsScreen(
                shows = adminShowsViewModel.shows.value
            )
        }
    }
}