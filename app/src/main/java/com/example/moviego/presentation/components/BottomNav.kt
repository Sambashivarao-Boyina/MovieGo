package com.example.moviego.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.moviego.R
import com.example.moviego.presentation.navgraph.Route
import com.example.moviego.ui.theme.Black111
import com.example.moviego.ui.theme.Black161
import com.example.moviego.ui.theme.Black1C1
import com.example.moviego.ui.theme.RedBB0

@Composable
fun BottomBar(
    navController: NavHostController,
    navItems: List<BottomNavItem>
) {
    val backstack = navController.currentBackStackEntryAsState().value
    val currentRoute = remember(key1 = backstack) {
        backstack?.destination?.route
    }
    Row(
        modifier = Modifier
            .background(Black111)
            .padding(vertical = 10.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .height(70.dp)
                .clip(RoundedCornerShape(50.dp))
                .background(
                    Black1C1
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            navItems.forEach { item ->
                NavigationBarItem(
                    selected = item.route == currentRoute,
                    onClick = {
                        navController.navigate(item.route) {
                            navController.graph.startDestinationRoute?.let { homeScreen ->
                                popUpTo(homeScreen) {
                                    saveState = true
                                }
                                restoreState = true
                                launchSingleTop = true
                            }
                        }
                    },
                    icon = {
                        Icon(painter = painterResource(item.icon), contentDescription = null, modifier = Modifier.size(35.dp))
                    },
                    colors = NavigationBarItemDefaults.colors().copy(
                        selectedIconColor = RedBB0,
                        selectedTextColor = RedBB0,
                        unselectedIconColor = Color.Gray,
                        unselectedTextColor = Color.Gray,
                        selectedIndicatorColor = Color.Transparent
                    )
                )
            }
        }
    }
}

data class BottomNavItem(val route:String, val icon: Int, val label:String)