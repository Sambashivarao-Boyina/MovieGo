package com.example.moviego.presentation.user.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.moviego.domain.model.Movie
import com.example.moviego.presentation.components.MovieCard
import com.example.moviego.presentation.navgraph.Route

@Composable
fun UserHomeScreen(
    movies: List<Movie>,
    isLoading: Boolean,
    navController: NavHostController
) {
    Scaffold {
        if(isLoading) {
            Column(
                modifier = Modifier.padding().fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier.padding(it)
            ) {

                if(movies.isEmpty()) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("No Movies Available")
                    }
                } else {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2), // 2 columns per row
                        modifier = Modifier.padding(8.dp),
                        contentPadding = PaddingValues(8.dp)
                    ) {
                        items(movies) { movie ->
                            MovieCard(
                                movie = movie,
                                onClick = {
                                    navController.navigate(Route.UserMovieDetails.passMovieId(movie._id))
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}