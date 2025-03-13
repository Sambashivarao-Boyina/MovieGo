package com.example.moviego.presentation.admin

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.moviego.R
import com.example.moviego.presentation.components.BottomBar
import com.example.moviego.presentation.components.BottomNavItem
import com.example.moviego.presentation.navgraph.Route
import com.example.moviego.presentation.navgraph.admin_navgraph.AdminNavGraph

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AdminNavigator() {
    val navController = rememberNavController()
    val backstack = navController.currentBackStackEntryAsState().value

    val isBottomBarVisible = remember(key1 = backstack) {
        backstack?.destination?.route == Route.AdminShows.route ||
                backstack?.destination?.route == Route.AdminAddShow.route ||
                backstack?.destination?.route == Route.AdminDetails.route
    }
    Scaffold(
        bottomBar = {
            if(isBottomBarVisible) {
                BottomBar(
                    navController = navController,
                    navItems = listOf(
                        BottomNavItem(
                            label = "Shows",
                            route = Route.AdminShows.route,
                            icon = R.drawable.ticket
                        ),
                        BottomNavItem(
                            label = "Add",
                            route = Route.AdminAddShow.route,
                            icon = R.drawable.add
                        ),
                        BottomNavItem(
                            label = "Profile",
                            route = Route.AdminDetails.route,
                            icon = R.drawable.profile
                        )
                    )
                )
            }
        }
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(bottom = it.calculateBottomPadding())
        ) {
            AdminNavGraph(navController = navController)
        }
    }
}