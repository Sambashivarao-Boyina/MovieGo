package com.example.moviego.presentation.admin.movies

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.moviego.domain.model.Movie
import com.example.moviego.presentation.admin.components.TopBar
import com.example.moviego.presentation.components.MovieCard
import com.example.moviego.presentation.navgraph.Route
import com.example.moviego.ui.theme.RedE31


@Composable
fun AdminMoviesScreen(
    movies: List<Movie>,
    isLoading: Boolean,
    navController: NavHostController
){

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate(Route.AdminAddMovie.route)
                },
                shape = CircleShape,
                containerColor = RedE31
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null)
            }
        },
        topBar = {
            TopBar(
                title = "Movies",
                navigationBox = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = "back",
                            tint = Color.White
                        )
                    }
                }
            )
        }
    ) {
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
                               navController.navigate(Route.AdminMovieDetails.passMovieId(movie._id))
                           }
                       )
                   }
               }
           }
       }
    }

}