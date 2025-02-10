package com.example.moviego.presentation.navgraph.auth_navgraph

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.moviego.presentation.authentication.adminAuth.admin_login.AdminLoginEvent
import com.example.moviego.presentation.authentication.adminAuth.admin_login.AdminLoginScreen
import com.example.moviego.presentation.authentication.adminAuth.admin_login.AdminLoginViewModel
import com.example.moviego.presentation.authentication.adminAuth.admin_signup.AdminSignUpEvent
import com.example.moviego.presentation.authentication.adminAuth.admin_signup.AdminSignUpScreen
import com.example.moviego.presentation.authentication.adminAuth.admin_signup.AdminSignUpViewmodel
import com.example.moviego.presentation.authentication.auth_options.AuthOptionsScreen
import com.example.moviego.presentation.authentication.userAuth.user_login.UserLoginEvent
import com.example.moviego.presentation.authentication.userAuth.user_login.UserLoginScreen
import com.example.moviego.presentation.authentication.userAuth.user_login.UserLoginViewModel
import com.example.moviego.presentation.authentication.userAuth.user_signup.UserSignUpEvent
import com.example.moviego.presentation.authentication.userAuth.user_signup.UserSignUpScreen
import com.example.moviego.presentation.authentication.userAuth.user_signup.UserSignUpViewModel
import com.example.moviego.presentation.navgraph.Route

@Composable
fun AuthNavGraph() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Route.AuthOptionScreen.route) {
        composable(route = Route.AuthOptionScreen.route) {
            AuthOptionsScreen(navController = navController)
        }

        composable(route = Route.UserSignUpScreen.route) {
            val userSignUpViewmodel: UserSignUpViewModel = hiltViewModel()
            if(userSignUpViewmodel.sideEffect != null) {
                Toast.makeText(
                    LocalContext.current,
                    userSignUpViewmodel.sideEffect,
                    Toast.LENGTH_SHORT
                ).show()
                userSignUpViewmodel.onEvent(UserSignUpEvent.RemoveSideEffect)
            }
            UserSignUpScreen(
                onEvent = userSignUpViewmodel::onEvent,
                isLoading = userSignUpViewmodel.isLoading,
                state = userSignUpViewmodel.state.value,
                navController = navController
            )
        }

        composable(route = Route.UserLoginScreen.route) {
            val userLoginViewModel: UserLoginViewModel = hiltViewModel()
            if(userLoginViewModel.sideEffect != null) {
                Toast.makeText(
                    LocalContext.current,
                    userLoginViewModel.sideEffect,
                    Toast.LENGTH_SHORT
                ).show()
                userLoginViewModel.onEvent(UserLoginEvent.RemoveSideEffect)
            }
            UserLoginScreen(
                onEvent = userLoginViewModel::onEvent,
                isLoading = userLoginViewModel.isLoading,
                state = userLoginViewModel.state.value,
                navController = navController
            )
        }

        composable(route = Route.AdminLoginScreen.route) {
            val adminLoginViewModel: AdminLoginViewModel = hiltViewModel()

            if(adminLoginViewModel.sideEffect != null) {
                Toast.makeText(
                    LocalContext.current,
                    adminLoginViewModel.sideEffect,
                    Toast.LENGTH_SHORT
                ).show()
                adminLoginViewModel.onEvent(AdminLoginEvent.RemoveSideEffect)
            }

            AdminLoginScreen(
                navController = navController,
                onEvent = adminLoginViewModel::onEvent,
                state = adminLoginViewModel.state.value,
                isLoading = adminLoginViewModel.isLoading
            )
        }

        composable(route = Route.AdminSignUpScreen.route) {
            val adminSignUpViewmodel: AdminSignUpViewmodel = hiltViewModel()
            if(adminSignUpViewmodel.sideEffect != null) {
                Toast.makeText(
                    LocalContext.current,
                    adminSignUpViewmodel.sideEffect,
                    Toast.LENGTH_SHORT
                ).show()
                adminSignUpViewmodel.onEvent(AdminSignUpEvent.RemoveSideEffect)
            }

            AdminSignUpScreen(
                state = adminSignUpViewmodel.state.value,
                onEvent = adminSignUpViewmodel::onEvent,
                isLoading = adminSignUpViewmodel.isLoading,
                navController = navController
            )
        }
    }
}