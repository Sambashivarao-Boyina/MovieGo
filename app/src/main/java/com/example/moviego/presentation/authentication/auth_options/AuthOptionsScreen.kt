package com.example.moviego.presentation.authentication.auth_options

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.moviego.R
import com.example.moviego.presentation.authentication.components.PersonCard
import com.example.moviego.presentation.navgraph.Route
import com.example.moviego.ui.theme.RedE31

@Composable
fun AuthOptionsScreen(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(30.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(R.drawable.moviego),
            contentDescription = null,
            modifier = Modifier.size(150.dp)
                .clip(CircleShape)
        )
        Text("MovieGo", style = MaterialTheme.typography.displayLarge, color = RedE31)
        Text(text = "Your favorite movie booking destination", textAlign = TextAlign.Center, color = Color.Gray, style = MaterialTheme.typography.titleMedium)
        Text(text = "How would you like to sign in?", textAlign = TextAlign.Center, style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(10.dp))
        PersonCard(
            image = R.drawable.shield,
            title = "Admin",
            description = "Manage theaters, movies, and administrative functions",
            onClick = {
                navController.navigate(Route.AdminLoginScreen.route)
            }
        )
        Spacer(modifier = Modifier.height(10.dp))
        PersonCard(
            image = R.drawable.profile,
            title = "User",
            description = "Book tickets, check show times, and manage your reservations",
            onClick = {
                navController.navigate(Route.UserLoginScreen.route)
            }
        )


    }
}