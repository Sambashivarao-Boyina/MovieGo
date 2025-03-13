package com.example.moviego.presentation.admin.admin_theaters

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.moviego.domain.model.TheaterDetails
import com.example.moviego.presentation.admin.components.TopBar
import com.example.moviego.presentation.components.TheaterCard
import com.example.moviego.presentation.navgraph.Route
import com.example.moviego.ui.theme.RedE31

@Composable
fun AdminTheatersScreen(
    theaters: List<TheaterDetails>,
    isLoading: Boolean,
    navController: NavHostController
) {

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                },
                shape = CircleShape,
                containerColor = RedE31
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null)
            }
        },
        topBar = {
            TopBar(
                title = "Theaters",
                navigationBox = {
                    IconButton(
                        onClick = {
                            navController.popBackStack()
                        }
                    ) {
                        Icon(imageVector = Icons.AutoMirrored.Default.ArrowBack, contentDescription = "back")
                    }
                }
            )
        }
    ) {
        if(theaters.isEmpty()) {
            Column(
                modifier = Modifier.padding(it).fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("No Theaters Available")
            }
        } else {
            Column(
                modifier = Modifier.padding(it).fillMaxSize()
            ) {
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

}


