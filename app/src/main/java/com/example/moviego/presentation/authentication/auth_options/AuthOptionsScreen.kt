package com.example.moviego.presentation.authentication.auth_options

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.moviego.R
import com.example.moviego.presentation.authentication.components.PersonCard
import com.example.moviego.presentation.navgraph.Route

@Composable
fun AuthOptionsScreen(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(30.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        PersonCard(
            image = R.drawable.movie_admin,
            title = "Admin",
            description = "Manage movies, bookings, and users seamlessly!",
            onClick = {
                navController.navigate(Route.AdminLoginScreen.route)
            }
        )
        Spacer(modifier = Modifier.height(30.dp))
        PersonCard(
            image = R.drawable.movie_user,
            title = "User",
            description = "Book your favorite movie tickets now!",
            onClick = {
                navController.navigate(Route.UserLoginScreen.route)
            }
        )


    }
}