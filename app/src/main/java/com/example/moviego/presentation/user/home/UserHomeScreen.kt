package com.example.moviego.presentation.user.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.moviego.R
import com.example.moviego.domain.model.Movie
import com.example.moviego.presentation.admin.add_theater.Root
import com.example.moviego.presentation.admin.add_theater.readRawResource
import com.example.moviego.presentation.admin.shows.FilterCard
import com.example.moviego.presentation.authentication.components.InputBox
import com.example.moviego.presentation.components.DropDownItem
import com.example.moviego.presentation.components.DropDownSelect
import com.example.moviego.presentation.components.MovieCard
import com.example.moviego.presentation.components.shimmerEffect
import com.example.moviego.presentation.navgraph.Route
import com.example.moviego.ui.theme.Black111
import com.example.moviego.ui.theme.Black161
import com.example.moviego.ui.theme.Black1C1
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
    selectedGenres: Set<String>,
    genres: List<String>,
    selectedLanguage: Set<String>,
    languages: List<String>
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

    var showFilters by rememberSaveable {
        mutableStateOf(false)
    }
    var selectedFilterOptionToShow by rememberSaveable {
        mutableStateOf<FilterOption>(FilterOption.Genre)
    }



    LaunchedEffect(key1 = location) {
        if (location == "") {
            showCitySelectionPopup = true
        } else {
            onEvent(UserHomeEvent.LoadMovies)
        }
    }

    Scaffold(
    ) {
        if (isLoading) {
            Column(
                modifier = Modifier.padding(it)
            ) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2), // 2 columns per row
                    modifier = Modifier.padding(8.dp),
                    contentPadding = PaddingValues(8.dp)
                ) {
                    items(6) {
                        Box(
                            modifier = Modifier
                                .padding(10.dp)
                                .height(300.dp)
                                .fillMaxWidth()
                                .clip(
                                    RoundedCornerShape(20.dp)
                                )
                                .shimmerEffect()
                        )
                    }
                }
            }
        } else {
            Column(
                modifier = Modifier.padding(it)
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            showCitySelectionPopup = true
                        }
                        .padding(horizontal = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
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
                            if (location == "") {
                                Text("Set the Location")
                            } else {
                                Text(
                                    location.split(".")[1],
                                    style = MaterialTheme.typography.titleLarge,
                                    color = RedE31
                                )
                                Text(
                                    location.split(".")[0],
                                    style = MaterialTheme.typography.titleSmall,
                                    color = Color.Gray
                                )
                            }
                        }
                    }
                    Button(
                        onClick = {
                            showFilters = true
                        }
                    ) {
                        Text(text = "Filters")
                    }
                }

                if (selectedGenres.isNotEmpty()) {
                    LazyRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp, vertical = 5.dp),
                        horizontalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        items(selectedGenres.toList()) {
                            FilterCard(
                                title = it,
                                onClick = {
                                    onEvent(UserHomeEvent.RemoveGenreFilter(it))
                                }
                            )
                        }

                        items(selectedLanguage.toList()) {
                            FilterCard(
                                title = it,
                                onClick = {
                                    onEvent(UserHomeEvent.RemoveLanguageFilter(it))
                                }
                            )
                        }
                    }
                }
                if (movies.isEmpty()) {
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

            if (showCitySelectionPopup) {
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
                                    ?.filter { city ->
                                        city.city.lowercase().contains(citySearch.lowercase())
                                    }
                            }
                        }

                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(max = 400.dp)
                        ) {
                            filteredCities?.let { list ->
                                if (list.size > 0) {
                                    items(list) { c ->
                                        Text(
                                            text = c.city,
                                            modifier = Modifier
                                                .clickable {
                                                    onEvent(UserHomeEvent.SetNewCity(c.city))
                                                    showCitySelectionPopup = false
                                                }
                                                .padding(5.dp)
                                        )
                                    }
                                } else {
                                    item {
                                        Text(
                                            text = "No Cities Found ",
                                            textAlign = TextAlign.Center,
                                            modifier = Modifier.fillMaxWidth()
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(Modifier.height(50.dp))

                    }
                }
            }

            if (showFilters) {
                FiltersModalSheet(
                    onClose = {
                        showFilters = false
                    },
                    selectedLanguage = selectedLanguage,
                    selectedGenres = selectedGenres,
                    genres = genres,
                    languages = languages,
                    onEvent = onEvent,
                    setOptionLanguage = {
                        selectedFilterOptionToShow = FilterOption.Language
                    },
                    setOptionGenre = {
                        selectedFilterOptionToShow = FilterOption.Genre
                    },
                    selectedFilterOption = selectedFilterOptionToShow
                )
            }

        }
    }
}

@Composable
fun FilterItem(
    title: String,
    onClick: () -> Unit,
    isChecked: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = isChecked,
            onCheckedChange = {
                onClick()
            }
        )

        Text(text = title)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FiltersModalSheet(
    onClose: () -> Unit,
    genres: List<String>,
    selectedGenres: Set<String>,
    onEvent: (UserHomeEvent) -> Unit,
    selectedLanguage: Set<String>,
    languages: List<String>,
    setOptionLanguage: () -> Unit,
    setOptionGenre: () -> Unit,
    selectedFilterOption: FilterOption
) {
    ModalBottomSheet(
        containerColor = Black161,
        onDismissRequest = {
            onClose()
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

            Text("Select the Genres")

            Row(
                modifier = Modifier
                    .background(Black161)
                    .heightIn(max = 300.dp)
                    .fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .background(Color.Black)
                        .weight(0.4f)
                        .fillMaxHeight()
                ) {
                    Text(
                        text = "Genre",
                        color = if (selectedFilterOption == FilterOption.Genre) RedE31 else Color.White,
                        modifier = Modifier
                            .fillMaxWidth()
                            .leftBorder(
                                borderColor = if (selectedFilterOption == FilterOption.Genre) RedE31 else Color.Transparent,
                                borderWidth = 5.dp
                            )
                            .clip(RoundedCornerShape(bottomEnd = if (selectedFilterOption == FilterOption.Language) 20.dp else 0.dp))
                            .background(
                                color = if (selectedFilterOption == FilterOption.Genre) Color.Black else Black161,
                            )
                            .clickable {
                                setOptionGenre()
                            }
                            .padding(horizontal = 10.dp, vertical = 10.dp),
                        fontSize = 20.sp
                    )
                    Text(
                        text = "Language",
                        color = if (selectedFilterOption == FilterOption.Language) RedE31 else Color.White,
                        modifier = Modifier
                            .fillMaxWidth()
                            .leftBorder(
                                borderColor = if (selectedFilterOption == FilterOption.Language) RedE31 else Color.Transparent,
                                borderWidth = 5.dp
                            )
                            .clip(RoundedCornerShape(topEnd = if (selectedFilterOption == FilterOption.Genre) 20.dp else 0.dp))

                            .background(
                                color = if (selectedFilterOption == FilterOption.Language) Color.Black else Black161,
                            )
                            .clickable {
                                setOptionLanguage()
                            }
                            .padding(horizontal = 10.dp, vertical = 10.dp),
                        fontSize = 20.sp
                    )

                    Spacer(
                        Modifier

                            .weight(1f)
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(topEnd = if (selectedFilterOption == FilterOption.Language) 20.dp else 0.dp))
                            .background(Black161)
                    )
                }

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clip(
                            RoundedCornerShape(
                                topStart = if (selectedFilterOption == FilterOption.Language) 20.dp else 0.dp,
                                topEnd = 20.dp,
                                bottomEnd = 20.dp,
                                bottomStart = 20.dp
                            )
                        )
                        .background(Color.Black)

                ) {
                    if (selectedFilterOption == FilterOption.Genre) {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight()
                        ) {

                            items(genres) { genre ->
                                FilterItem(
                                    title = genre,
                                    isChecked = selectedGenres.contains(genre),
                                    onClick = {
                                        if (selectedGenres.contains(genre)) {
                                            onEvent(UserHomeEvent.RemoveGenreFilter(genre))
                                        } else {
                                            onEvent(UserHomeEvent.AddGenreFilter(genre))
                                        }
                                    }
                                )
                            }
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight()
                        ) {

                            items(languages) { language ->
                                FilterItem(
                                    title = language,
                                    isChecked = selectedLanguage.contains(language),
                                    onClick = {
                                        if (selectedLanguage.contains(language)) {
                                            onEvent(UserHomeEvent.RemoveLanguageFilter(language))
                                        } else {
                                            onEvent(UserHomeEvent.AddLanguageFilter(language))
                                        }
                                    }
                                )
                            }
                        }
                    }

                }
            }


            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Button(
                    onClick = {
                        onClose()
                        onEvent(UserHomeEvent.ClearFilters)
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
                        onClose()
                    }
                ) {
                    Text(text = "Close")
                }
            }
        }
    }
}


enum class FilterOption {
    Genre,
    Language
}

fun Modifier.leftBorder(borderWidth: Dp, borderColor: Color): Modifier = this.then(
    Modifier.drawBehind {
        drawLine(
            color = borderColor,
            start = Offset(0f, 0f),
            end = Offset(0f, size.height),
            strokeWidth = borderWidth.toPx()
        )
    }
)