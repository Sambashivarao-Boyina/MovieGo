package com.example.moviego.presentation.navgraph

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.example.moviego.presentation.admin.AdminNavigator
import com.example.moviego.presentation.authentication.AuthNavigator
import com.example.moviego.presentation.navgraph.user_navgraph.UserNavGraph
import com.example.moviego.presentation.user.UserNavigator
import com.example.moviego.presentation.user.payment_confirmation.UserPaymentConfirmationViewModel


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavGraph(
    startDestination:String,
    userPaymentConfirmationViewModel: UserPaymentConfirmationViewModel
) {
    val navController = rememberNavController()
    LaunchedEffect(startDestination) {
        navController.navigate(startDestination){
            popUpTo(0) {
                inclusive = true
            }
            launchSingleTop = true
        }
    }

    NavHost(navController = navController, startDestination = startDestination) {
        navigation(
            route = Route.AuthRoutes.route,
            startDestination = Route.AuthNavigatorScreen.route
        ) {
            composable(route = Route.AuthNavigatorScreen.route) {
                AuthNavigator()
            }
        }

        navigation(
            route = Route.UserRoutes.route,
            startDestination = Route.UserNavigatorScreen.route,
        ) {
            composable(route = Route.UserNavigatorScreen.route) {
                UserNavigator(
                    userPaymentConfirmationViewModel = userPaymentConfirmationViewModel
                )
            }
        }

        navigation(
            route =Route.AdminRoutes.route,
            startDestination = Route.AdminNavigatorScreen.route
        ) {
            composable(route = Route.AdminNavigatorScreen.route) {
                AdminNavigator()
            }
        }
    }
}