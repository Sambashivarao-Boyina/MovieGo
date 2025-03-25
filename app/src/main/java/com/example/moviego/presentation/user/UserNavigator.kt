package com.example.moviego.presentation.user

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.moviego.R
import com.example.moviego.presentation.components.BottomBar
import com.example.moviego.presentation.components.BottomNavItem
import com.example.moviego.presentation.navgraph.Route
import com.example.moviego.presentation.navgraph.user_navgraph.UserNavGraph

@Composable
fun UserNavigator() {
    val navController = rememberNavController()
    val backstack = navController.currentBackStackEntryAsState().value

    val isBottomBarVisible = remember(key1 = backstack) {
        backstack?.destination?.route == Route.UserHomeRoute.route ||
                backstack?.destination?.route == Route.UserBookings.route ||
                backstack?.destination?.route == Route.UserDetails.route
    }
    Scaffold(
        bottomBar = {
            if(isBottomBarVisible) {
                BottomBar(
                    navController = navController,
                    navItems = listOf(
                        BottomNavItem(
                            label = "Home",
                            route = Route.UserHomeRoute.route,
                            icon = R.drawable.home
                        ),
                        BottomNavItem(
                            label = "Bookings",
                            route = Route.UserBookings.route,
                            icon = R.drawable.ticket
                        ),
                        BottomNavItem(
                            label = "Profile",
                            route = Route.UserDetails.route,
                            icon = R.drawable.profile
                        )
                    )
                )
            }
        }
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
                .padding(it)
        ) {
            UserNavGraph(navController = navController)
        }
    }
}