package com.example.moviego.presentation.admin.admin_theaters

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.moviego.domain.model.TheaterDetails
import com.example.moviego.presentation.components.MovieCard
import com.example.moviego.presentation.components.TheaterCard

@Composable
fun AdminTheatersScreen(
    theaters: List<TheaterDetails>,
    isLoading: Boolean
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
        LazyVerticalGrid(
            columns = GridCells.Fixed(2), // 2 theaters per row
            modifier = Modifier.padding(8.dp),
            contentPadding = PaddingValues(8.dp)
        ) {
            items(theaters) { theater ->
                TheaterCard(theater, onClick = {})
            }
        }
    }

}


