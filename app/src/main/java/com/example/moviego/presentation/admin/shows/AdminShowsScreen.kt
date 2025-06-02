package com.example.moviego.presentation.admin.shows

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.moviego.domain.model.Show
import com.example.moviego.presentation.admin.components.ShowCard
import com.example.moviego.presentation.components.DatePicker
import com.example.moviego.presentation.components.DropDownItem
import com.example.moviego.presentation.components.DropDownSelect
import com.example.moviego.presentation.components.shimmerEffect
import com.example.moviego.presentation.navgraph.Route
import com.example.moviego.ui.theme.Black111
import com.example.moviego.ui.theme.Black161

@SuppressLint("NewApi")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminShowsScreen(
    shows: List<Show>,
    isLoading: Boolean,
    onEvent: (AdminShowEvent) -> Unit,
    navController: NavHostController,
    filterOptions: FilterOptions,
    selectedFilters: SelectedFilters
) {
    var showFilters by rememberSaveable {
        mutableStateOf(false)
    }
    Scaffold {
        if(isLoading) {
            LazyColumn(
                modifier = Modifier.padding(it),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(5) {
                    ShimmeringBookingCard()
                }
            }
        } else{
            Column(
                modifier = Modifier.padding(it)
            ) {
                Row(
                    modifier = Modifier
                        .padding(end = 20.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End
                ) {

                    Button(
                        onClick = {
                            showFilters = true
                        }
                    ) {
                        Text(text = "Filters")
                    }
                }

                LazyRow(
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    if (selectedFilters.date.isNotEmpty()) {
                        item {
                            FilterCard(title = selectedFilters.date) {
                                onEvent(AdminShowEvent.UpdateFilterDate(""))
                            }
                        }
                    }
                    if (selectedFilters.movie != null) {
                        item {
                            FilterCard(title = selectedFilters.movie.Title) {
                                onEvent(AdminShowEvent.UpdateFilterMovie(null))

                            }
                        }
                    }
                    if (selectedFilters.theater != null) {
                        item {
                            FilterCard(title = selectedFilters.theater.name) {
                                onEvent(AdminShowEvent.UpdateFilterTheater(null))
                            }
                        }
                    }
                }
                if (shows.isEmpty()) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("No Shows Available ", style = MaterialTheme.typography.titleLarge)
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        items(shows) { show ->
                            ShowCard(show = show, onClick = {
                                navController.navigate(Route.AdminShowDetails.passShowId(showId = show._id))
                            })
                        }
                    }
                }

                if (showFilters) {
                    ModalBottomSheet(
                        containerColor = Black161,
                        onDismissRequest = {
                            showFilters = false
                        }
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 10.dp)
                                .padding(bottom = 20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            DatePicker(
                                selectedDate = selectedFilters.date,
                                onSelected = {
                                    onEvent(AdminShowEvent.UpdateFilterDate(it))
                                },
                                error = "",
                                allowPastDates = true
                            )

                            DropDownSelect(
                                items = filterOptions.movies.map {
                                    DropDownItem(title = it.Title, ref = it._id)
                                },
                                onSelect = {
                                    onEvent(AdminShowEvent.UpdateFilterMovie(filterOptions.movies.find { movie ->
                                        movie._id == it.ref
                                    }))
                                },
                                error = "",
                                unAvailableMessage = "Select Movie",
                                initialValue = if(selectedFilters.movie !== null) DropDownItem(
                                    title = selectedFilters.movie.Title,
                                    ref = selectedFilters.movie._id
                                ) else null
                            )
                            DropDownSelect(
                                items = filterOptions.theaters.map {
                                    DropDownItem(title = it.name, ref = it._id)
                                },
                                onSelect = {
                                    onEvent(AdminShowEvent.UpdateFilterTheater(filterOptions.theaters.find { theater ->
                                        theater._id == it.ref
                                    }))
                                },
                                error = "",
                                unAvailableMessage = "Select Theater",
                                initialValue = if(selectedFilters.theater !== null) DropDownItem(
                                    title = selectedFilters.theater.name,
                                    ref = selectedFilters.theater._id
                                ) else null
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End
                            ) {
                                Button(
                                    onClick = {
                                        onEvent(AdminShowEvent.UpdateFilterMovie(null))
                                        onEvent(AdminShowEvent.UpdateFilterTheater(null))
                                        onEvent(AdminShowEvent.UpdateFilterDate(""))
                                        showFilters = false
                                    }
                                ) {
                                    Text(text = "Clear Filters")
                                }
                                Spacer(Modifier.width(20.dp))
                                Button(
                                    colors = ButtonDefaults.buttonColors().copy(
                                        containerColor = Black111,
                                        contentColor = Color.White
                                    ),
                                    onClick = {
                                        showFilters = false
                                    }
                                ) {
                                    Text(text = "Close")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FilterCard(
    title: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .clip(
                RoundedCornerShape(20.dp)
            )
            .background(Black161)
            .padding(start = 20.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(text = title)
        IconButton(
            onClick = onClick
        ) {
            Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
        }
    }
}

@Composable
fun ShimmeringBookingCard(modifier: Modifier = Modifier) {
    Row(
        modifier = Modifier.height(200.dp).fillMaxWidth()
    ) {
        Box(modifier = Modifier.clip(RoundedCornerShape(20.dp)).fillMaxHeight().weight(0.4f).shimmerEffect())
        Column(
            modifier = modifier
                .weight(1f)
                .height(200.dp) // Adjust the height to match your card's layout
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Placeholder for the header (e.g., booking ID, status)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Box(modifier = Modifier.size(80.dp, 20.dp).shimmerEffect())
                Box(modifier = Modifier.size(60.dp, 20.dp).shimmerEffect())
            }

            // Placeholder for movie title and details
            Column {
                Box(modifier = Modifier.fillMaxWidth(0.7f).height(24.dp).shimmerEffect())
                Spacer(modifier = Modifier.height(8.dp))
                Box(modifier = Modifier.fillMaxWidth(0.5f).height(16.dp).shimmerEffect())
                Spacer(modifier = Modifier.height(4.dp))
                Box(modifier = Modifier.fillMaxWidth(0.6f).height(16.dp).shimmerEffect())
            }

            // Placeholder for booking details (date, time, seats)
            Column {
                Box(modifier = Modifier.fillMaxWidth(0.4f).height(16.dp).shimmerEffect())
                Spacer(modifier = Modifier.height(4.dp))
                Box(modifier = Modifier.fillMaxWidth(0.3f).height(16.dp).shimmerEffect())
            }

            // Placeholder for ticket cost and booked seats
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Box(modifier = Modifier.size(80.dp, 20.dp).shimmerEffect())
            }
        }
    }
}
