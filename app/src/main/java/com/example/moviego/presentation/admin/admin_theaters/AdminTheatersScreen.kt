package com.example.moviego.presentation.admin.admin_theaters

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.moviego.domain.model.TheaterDetails
import com.example.moviego.presentation.components.TheaterCard
import com.example.moviego.presentation.navgraph.Route

@Composable
fun AdminTheatersScreen(
    theaters: List<TheaterDetails>,
    isLoading: Boolean,
    navController: NavHostController
) {

    if(theaters.isEmpty()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("No Theaters Available")
        }
    } else {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Text("Theaters", style = MaterialTheme.typography.displaySmall, modifier = Modifier.padding(horizontal = 20.dp))
            LazyColumn(
                modifier = Modifier.padding(8.dp),
                contentPadding = PaddingValues(8.dp)
            ) {
                items(theaters) { theater ->
                    TheaterCard(theater, onClick = {
                        navController.navigate(Route.AdminTheaterDetails.passTheaterId(theater._id))
                    })
                }
            }
        }

    }

}


