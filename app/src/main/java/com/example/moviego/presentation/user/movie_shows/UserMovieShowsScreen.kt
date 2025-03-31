package com.example.moviego.presentation.user.movie_shows

import android.annotation.SuppressLint
import androidx.annotation.ColorRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
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
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.moviego.domain.model.Movie
import com.example.moviego.domain.model.Screen
import com.example.moviego.domain.model.Show
import com.example.moviego.domain.model.Theater
import com.example.moviego.presentation.admin.components.TopBar
import com.example.moviego.ui.theme.Black111
import com.example.moviego.ui.theme.Black161
import com.example.moviego.ui.theme.Black1C1
import com.example.moviego.ui.theme.RedE31
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@SuppressLint("NewApi")
@Composable
fun UserMovieShowsScreen(
    isLoading: Boolean,
    movie: Movie?,
    shows: Map<String, Map<Theater, Map<Screen, List<Show>>>>,
    onEvent: (UserMovieShowsEvent) -> Unit,
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
        if (isLoading) {
            Column(
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator()
            }
        } else {
            if (shows.isEmpty()) {
                Column(
                    modifier = Modifier
                        .padding(it)
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "No Shows Available for this Movie",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            } else {
                val dates = rememberSaveable { getNextSevenDays() }
                var selectedDate by rememberSaveable {
                    mutableStateOf(dates[0]) // Save as String
                }
                val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")

                Column(
                    modifier = Modifier
                        .padding(it)
                        .fillMaxSize()
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        dates.map {
                            Column(
                                modifier = Modifier
                                    .clickable {
                                        selectedDate = it
                                    }
                                    .background(
                                        if (it === selectedDate) RedE31 else Color.Transparent
                                    )
                                    .weight(1f)
                                    .padding(vertical = 10.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = it.dayOfWeek.toString().substring(0, 3),
                                    style = MaterialTheme.typography.titleSmall,
                                    color = if (it === selectedDate) Color.White.copy(alpha = 0.9f) else Color.Gray
                                )
                                Text(
                                    text = if (it.dayOfMonth < 9) "0${it.dayOfMonth}" else "${it.dayOfMonth}",
                                    style = MaterialTheme.typography.titleLarge
                                )
                                Text(
                                    text = it.month.toString().substring(0, 3),
                                    style = MaterialTheme.typography.titleSmall,
                                    color = if (it === selectedDate) Color.White.copy(alpha = 0.9f) else Color.Gray
                                )
                            }
                        }
                    }
                    val theaters = shows[selectedDate.format(formatter)] ?: emptyMap() // Get data for the date

                    LazyColumn(modifier = Modifier.fillMaxSize().padding(8.dp)) {
                        items(theaters.keys.toList()) { theater ->
                            TheaterItem(theater, theaters[theater] ?: emptyMap())
                        }
                    }

                }
            }
        }
    }
}


// Function to get the next 7 days including today
@SuppressLint("NewApi")
fun getNextSevenDays(): List<LocalDate> {
    val today = LocalDate.now()
    return List(7) { today.plusDays(it.toLong()) }
}

@Composable
fun TheaterItem(theater: Theater, screens: Map<Screen, List<Show>>) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            ,
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth().background(Black161).padding(16.dp)) {
            Text(
                text = theater.name,
                style = MaterialTheme.typography.titleLarge,
                color = RedE31
            )
            Text(
                text = "${theater.city}, ${theater.state}, ${theater.pincode}"
            )

            screens.forEach { (screen, shows) ->
                ScreenItem(screen, shows)
            }
        }
    }
}
@Composable
fun ScreenItem(screen: Screen, shows: List<Show>) {
    Column(modifier = Modifier.padding(start = 16.dp, top = 8.dp)) {
        Row(
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = screen.screenName,
                style = MaterialTheme.typography.titleLarge,
            )
            Text(
                text = "${screen.screenType}, ${screen.soundType}",
                style = MaterialTheme.typography.titleSmall
            )
        }

        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Spacer(modifier = Modifier.height(10.dp))
            shows.chunked(3).forEach { rowItems ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    rowItems.forEach { show ->
                        Text(
                            text = show.showTime,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier
                                .clickable { }
                                .weight(1f) // Makes each item occupy 1/3 of the row
                                .border(
                                    width = 2.dp,
                                    color = RedE31.copy(alpha = 0.6f),
                                    shape = RoundedCornerShape(10.dp)
                                )
                                .background(RedE31.copy(alpha = 0.1f))
                                .padding(vertical = 10.dp),
                            textAlign = TextAlign.Center
                        )
                    }

                    // If the last row has less than 3 items, fill the remaining space
                    repeat(3 - rowItems.size) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
            }
        }

    }
}
