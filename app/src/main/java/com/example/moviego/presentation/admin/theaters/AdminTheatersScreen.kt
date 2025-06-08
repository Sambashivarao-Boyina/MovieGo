package com.example.moviego.presentation.admin.theaters

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.moviego.domain.model.TheaterDetails
import com.example.moviego.presentation.admin.components.TopBar
import com.example.moviego.presentation.admin.movies.AdminMoviesEvent
import com.example.moviego.presentation.components.TheaterCard
import com.example.moviego.presentation.components.shimmerEffect
import com.example.moviego.presentation.navgraph.Route
import com.example.moviego.ui.theme.RedE31

@Composable
fun AdminTheatersScreen(
    theaters: List<TheaterDetails>,
    isLoading: Boolean,
    navController: NavHostController,
    onEvent: (AdminTheatersEvent)->Unit
) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate(Route.AdminAddTheater.route)
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
        val currentBackStackEntry = navController.currentBackStackEntryAsState().value
        val savedStateHandle = currentBackStackEntry?.savedStateHandle

        LaunchedEffect(key1 = savedStateHandle) {
            savedStateHandle?.getLiveData<Boolean>("refresh")?.observeForever { shouldRefresh ->
                if (shouldRefresh == true) {
                    onEvent(AdminTheatersEvent.ReloadTheaters)
                    savedStateHandle.remove<Boolean>("refresh")
                }
            }
        }
        if(isLoading) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(20.dp),
            ) {
                for(i in 1..5) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier.fillMaxWidth().height(200.dp).clip(
                                RoundedCornerShape(20.dp)
                            ).shimmerEffect()
                        )
                        Box(
                            modifier = Modifier.fillMaxWidth(0.5f).height(20.dp).clip(
                                RoundedCornerShape(20.dp)
                            ).shimmerEffect()
                        )
                        Box(
                            modifier = Modifier.fillMaxWidth(0.6f).height(20.dp).clip(
                                RoundedCornerShape(20.dp)
                            ).shimmerEffect()
                        )
                        Box(
                            modifier = Modifier.fillMaxWidth(0.2f).height(20.dp).clip(
                                RoundedCornerShape(20.dp)
                            ).shimmerEffect()
                        )
                    }
                }
            }
        } else {
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
                            }, onEditClick = {
                                navController.navigate(Route.AdminEditTheater.passTheaterId(theater._id))
                            })
                        }
                        item {
                            Spacer(modifier = Modifier.height(100.dp))
                        }
                    }
                }

            }
        }
    }

}


