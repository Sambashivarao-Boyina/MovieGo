package com.example.moviego.presentation.components

import androidx.compose.foundation.layout.size
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.moviego.ui.theme.Black1C1
import com.example.moviego.ui.theme.RedE31

@Composable
fun BottomBar(
    navController: NavHostController,
    navItems: List<BottomNavItem>
) {
    val backstack = navController.currentBackStackEntryAsState().value
    val currentRoute = remember(key1 = backstack) {
        backstack?.destination?.route
    }
    BottomAppBar(
        actions = {
            navItems.map {
                NavigationBarItem(
                    selected = currentRoute === it.route,
                    onClick = {
                        navController.navigate(it.route) {
                            navController.graph.startDestinationRoute?.let { homeScreen ->
                                popUpTo(homeScreen) {
                                    saveState = true
                                }
                                restoreState = true
                                launchSingleTop = true
                            }
                        }
                    },
                    label = {
                        Text(text = it.label)
                    },
                    icon = {
                        Icon(painter = painterResource(it.icon), contentDescription = it.label,modifier = Modifier.size(30.dp))
                    },
                    colors = NavigationBarItemDefaults.colors().copy(
                        selectedTextColor = RedE31,
                        selectedIconColor = RedE31,
                        selectedIndicatorColor = RedE31.copy(alpha = 0.1f)
                    )
                )
            }
        },
        containerColor = Black1C1,
        tonalElevation = 0.dp,
        contentColor = Color.Red
    )
}

data class BottomNavItem(val route:String, val icon: Int, val label:String)