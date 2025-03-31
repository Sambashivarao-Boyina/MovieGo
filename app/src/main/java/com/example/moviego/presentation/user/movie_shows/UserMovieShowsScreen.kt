package com.example.moviego.presentation.user.movie_shows

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.moviego.domain.model.Movie
import com.example.moviego.domain.model.Screen
import com.example.moviego.domain.model.Show
import com.example.moviego.domain.model.Theater
import com.example.moviego.presentation.admin.components.AccentRed
import com.example.moviego.presentation.admin.components.BorderDark
import com.example.moviego.presentation.admin.components.DarkSurface
import com.example.moviego.presentation.admin.components.TextPrimary
import com.example.moviego.presentation.admin.components.TextSecondary
import com.example.moviego.presentation.admin.components.TopBar

@Composable
fun UserMovieShowsScreen(
    isLoading: Boolean,
    movie: Movie?,
    shows: Map<String,Map<Theater, Map<Screen, List<Show>>>>,
    onEvent:(UserMovieShowsEvent) -> Unit,
    navController: NavHostController
) {
    Scaffold(
        topBar = {
            TopBar(
                title = movie?.Title ?: "",
                navigationBox = {
                    IconButton(
                        onClick = {
                            navController.popBackStack()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = null
                        )
                    }
                }
            )
        }
    ) {
        if(isLoading) {
            Column(
                modifier = Modifier.padding(it).fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator()
            }
        } else {
            if(shows.isEmpty()) {
                Column(
                    modifier = Modifier.padding(it).fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("No Shows Available for this Movie", style = MaterialTheme.typography.bodyMedium)
                }
            } else {
                LazyColumn(
                    modifier = Modifier.padding(it).fillMaxSize()
                ) {

                }
            }
        }
    }
}

