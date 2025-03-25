package com.example.moviego.presentation.navgraph.user_navgraph

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.moviego.presentation.navgraph.Route
import com.example.moviego.presentation.user.bookings.UserBookingViewModel
import com.example.moviego.presentation.user.bookings.UserBookingsScreen
import com.example.moviego.presentation.user.details.UserDetailsScreen
import com.example.moviego.presentation.user.details.UserDetailsViewModel
import com.example.moviego.presentation.user.home.UserHomeScreen
import com.example.moviego.presentation.user.home.UserHomeViewModel

@Composable
fun UserNavGraph(
    navController: NavHostController
) {

    NavHost(
        navController = navController,
        startDestination = Route.UserHomeRoute.route
    ){
        composable(route = Route.UserHomeRoute.route){
            val userHomeViewModel: UserHomeViewModel = hiltViewModel()
            UserHomeScreen()
        }

        composable(route = Route.UserDetails.route) {
            val userDetailsViewModel: UserDetailsViewModel = hiltViewModel()
            UserDetailsScreen()
        }

        composable(route = Route.UserBookings.route) {
            val userBookingViewModel: UserBookingViewModel = hiltViewModel()
            UserBookingsScreen()
        }
    }
}