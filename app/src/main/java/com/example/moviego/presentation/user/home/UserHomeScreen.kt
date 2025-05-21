package com.example.moviego.presentation.user.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.moviego.R
import com.example.moviego.domain.model.Movie
import com.example.moviego.presentation.admin.add_theater.AdminAddTheaterEvent
import com.example.moviego.presentation.admin.add_theater.Root
import com.example.moviego.presentation.admin.add_theater.readRawResource
import com.example.moviego.presentation.authentication.components.InputBox
import com.example.moviego.presentation.components.DropDownItem
import com.example.moviego.presentation.components.DropDownSelect
import com.example.moviego.presentation.components.MovieCard
import com.example.moviego.presentation.navgraph.Route
import com.example.moviego.ui.theme.RedE31
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserHomeScreen(
    movies: List<Movie>,
    isLoading: Boolean,
    navController: NavHostController,
    location: String,
    onEvent: (UserHomeEvent) -> Unit,
    state: String,
    city: String
) {
    val context = LocalContext.current

    var showCitySelectionPopup by remember {
        mutableStateOf(false)
    }

    val gson = remember {
        Gson()
    }
    val states = remember {
        val statesJson = readRawResource(R.raw.states, context)
        gson.fromJson<List<String>>(statesJson, object : TypeToken<List<String>>() {}.type)
    }

    val citiesMap = remember {
        val jsonString = readRawResource(R.raw.state_cities, context)
        gson.fromJson(jsonString, Root::class.java)
    }

    var citySearch by remember {
        mutableStateOf("")
    }



    LaunchedEffect(key1 = location) {
        if(location == "") {
            showCitySelectionPopup = true
        } else {
            onEvent(UserHomeEvent.LoadMovies)
        }
    }

    Scaffold(
    ) {
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



                Row(
                    modifier = Modifier.clickable {
                        showCitySelectionPopup = true
                    }.padding(horizontal = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.location),
                            contentDescription = null,
                            modifier = Modifier.size(25.dp),
                            tint = RedE31
                        )
                        Spacer(Modifier.width(15.dp))
                        Column {
                            if(location == "") {
                                Text("Set the Location")
                            } else {
                                Text(location.split(".")[1], style = MaterialTheme.typography.titleLarge, color = RedE31)
                                Text(location.split(".")[0], style = MaterialTheme.typography.titleSmall, color = Color.Gray)
                            }
                        }
                    }
                }
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

            if(showCitySelectionPopup) {
                ModalBottomSheet(
                    onDismissRequest = {
                        showCitySelectionPopup = false
                    },
                    properties = ModalBottomSheetDefaults.properties(
                        shouldDismissOnBackPress = false
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(10.dp),
                        verticalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        Text(text = "Select the City", style = MaterialTheme.typography.titleLarge)
                        DropDownSelect(
                            items = states.map { item ->
                                DropDownItem(
                                    title = item,
                                    ref = item
                                )
                            },
                            onSelect = {
                                onEvent(UserHomeEvent.SetNewState(it.title))
                            },
                            error = "",
                            unAvailableMessage = "Select the State",
                            unSelectedMessage = "Select the State"
                        )

                        InputBox(
                            value = citySearch,
                            onChange = {
                                citySearch = it
                            },
                            placeHolder = "Search City",
                            keyboardType = KeyboardType.Text
                        )

                        val filteredCities by remember(state, citySearch) {
                            derivedStateOf {
                                citiesMap.states.find { s -> s.state == state }
                                    ?.cities
                                    ?.filter { city -> city.city.lowercase().contains(citySearch.lowercase()) }
                            }
                        }

                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(max = 400.dp)
                        ) {
                            filteredCities?.let { list ->
                                if(list.size > 0) {
                                    items(list) { c ->
                                        Text(
                                            text = c.city,
                                            modifier = Modifier.clickable {
                                                onEvent(UserHomeEvent.SetNewCity(c.city))
                                                showCitySelectionPopup = false
                                            }
                                                .padding(5.dp)
                                        )
                                    }
                                } else {
                                    item {
                                        Text(text = "No Cities Found ", textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
                                    }
                                }
                            }
                        }

                        Spacer(Modifier.height(50.dp))

                    }
                }
            }
        }
    }
}

