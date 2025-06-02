package com.example.moviego.presentation.admin.add_show

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.moviego.R
import com.example.moviego.domain.model.Movie
import com.example.moviego.domain.model.Screen
import com.example.moviego.domain.model.TheaterDetails
import com.example.moviego.presentation.authentication.components.SubmitButton
import com.example.moviego.presentation.components.DropDownItem
import com.example.moviego.presentation.components.DropDownSelect
import com.example.moviego.presentation.components.ShowDatePicker
import com.example.moviego.presentation.components.TheaterDetailRow
import com.example.moviego.presentation.components.TimePicker
import com.example.moviego.presentation.components.shimmerEffect
import com.example.moviego.ui.theme.Black161
import com.example.moviego.ui.theme.Black1C1
import com.example.moviego.ui.theme.RedE31
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AdminAddShowScreen(
    state: AdminAddShowState,
    isLoading: Boolean,
    onEvent: (AdminAddShowEvent) -> Unit,
    movies: List<Movie>,
    theaters: List<TheaterDetails>,
    isCreatingShow: Boolean,
    isCreatedNewShow: Boolean
) {

    val selectedTheater by remember(state.theater, theaters) {
        onEvent(AdminAddShowEvent.UpdateScreen(""))
        derivedStateOf {
            theaters.find { it._id === state.theater }
        }
    }



    val pagerState = rememberPagerState(
        initialPage = 0,
        initialPageOffsetFraction = 0f,
        pageCount = { 4 }
    )
    var lastAllowedPage by remember { mutableIntStateOf(0) }

    val coroutineScope = rememberCoroutineScope()


    LaunchedEffect(key1 = isCreatedNewShow) {
        if(isCreatedNewShow) {
            coroutineScope.launch {
                val next = 0
                lastAllowedPage = next
                pagerState.animateScrollToPage(
                    next,
                    animationSpec = tween(1000)
                )

            }
        }
        onEvent(AdminAddShowEvent.RemoveNewShowCreated)
    }

    Scaffold {
        if(isLoading) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(20.dp),
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth().height(20.dp).clip(RoundedCornerShape(10.dp)).shimmerEffect()
                )

                Box(
                    Modifier.fillMaxWidth(0.5f).height(400.dp).clip(RoundedCornerShape(20.dp)).shimmerEffect()
                )
                Box(
                    Modifier.fillMaxWidth(0.5f).height(400.dp).clip(RoundedCornerShape(20.dp)).shimmerEffect()
                )
            }
        } else {
            Column(
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize()
            ) {
                Box() {
                    Text(
                        text = "Add Show",
                        style = MaterialTheme.typography.displaySmall,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                }

                HorizontalPager(
                    state = pagerState,
                    userScrollEnabled = false, // completely disable default scroll
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) { page ->
                    when (page) {
                        0 -> {
                            Column(
                                modifier = Modifier.fillMaxSize(),
                            ) {
                                Text(
                                    "Select the Movie",
                                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp),
                                    style = MaterialTheme.typography.titleLarge
                                )
                                LazyVerticalGrid(
                                    modifier = Modifier.weight(1f),
                                    columns = GridCells.Fixed(2),
                                    contentPadding = PaddingValues(8.dp)
                                ) {

                                    items(movies) { movie ->
                                        MovieSelectCard(
                                            movie = movie,
                                            onClick = {
                                                onEvent(AdminAddShowEvent.UpdateMovie(movie._id))
                                                coroutineScope.launch {
                                                    val next =
                                                        (pagerState.currentPage + 1).coerceAtMost(3)
                                                    lastAllowedPage = next
                                                    pagerState.animateScrollToPage(
                                                        next,
                                                        animationSpec = tween(1000)
                                                    )

                                                }
                                            },
                                            selected = movie._id === state.movie
                                        )
                                    }

                                }
                            }
                        }

                        1 -> {
                            Column(
                                modifier = Modifier.fillMaxSize(),
                            ) {
                                Text(
                                    "Select the Theater",
                                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp),
                                    style = MaterialTheme.typography.titleLarge
                                )
                                LazyColumn(
                                    modifier = Modifier.weight(1f),
                                    verticalArrangement = Arrangement.spacedBy(10.dp)
                                ) {

                                    items(theaters) { theater ->
                                        TheateSelectionrCard(
                                            theater = theater,
                                            onClick = {
                                                onEvent(AdminAddShowEvent.UpdateTheater(theater._id))
                                                coroutineScope.launch {
                                                    val next =
                                                        (pagerState.currentPage + 1).coerceAtMost(3)
                                                    lastAllowedPage = next
                                                    pagerState.animateScrollToPage(
                                                        next,
                                                        animationSpec = tween(1000)
                                                    )

                                                }
                                            },
                                            selected = state.theater == theater._id
                                        )
                                    }

                                }
                            }
                        }

                        2 -> {
                            Column(
                                modifier = Modifier.fillMaxSize(),
                            ) {
                                Text(
                                    "Select the Screen",
                                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp),
                                    style = MaterialTheme.typography.titleLarge
                                )
                                LazyColumn(
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(horizontal = 10.dp),
                                    verticalArrangement = Arrangement.spacedBy(10.dp)
                                ) {

                                    if (selectedTheater != null) {
                                        items(selectedTheater!!.screens) { screen ->
                                            ScreenSelectionCard(
                                                screen = screen,
                                                onClick = {
                                                    onEvent(AdminAddShowEvent.UpdateScreen(screen._id))
                                                    coroutineScope.launch {
                                                        val next =
                                                            (pagerState.currentPage + 1).coerceAtMost(3)
                                                        lastAllowedPage = next
                                                        pagerState.animateScrollToPage(
                                                            next,
                                                            animationSpec = tween(1000)
                                                        )

                                                    }
                                                },
                                                selected = state.screen == screen._id
                                            )
                                        }
                                    }

                                }
                            }
                        }

                        3 -> {
                            Column(
                                modifier = Modifier.fillMaxSize()
                            ) {

                                LazyColumn(
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(horizontal = 10.dp),
                                    verticalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    item {
                                        Text(
                                            text = "Select the Date",
                                            modifier = Modifier.padding(bottom = 5.dp)
                                        )
                                        ShowDatePicker(
                                            selectedDate = state.date,
                                            onSelected = {
                                                onEvent(AdminAddShowEvent.UpdateDate(it))
                                            },
                                            error = state.isDateError
                                        )
                                        Spacer(modifier = Modifier.height(20.dp))
                                    }



                                    item {
                                        Text(
                                            text = "Select the Time",
                                            modifier = Modifier.padding(bottom = 5.dp)
                                        )
                                        TimePicker(
                                            selectedTime = state.showTime,
                                            onSelected = {
                                                onEvent(AdminAddShowEvent.UpdateShowTime(it))
                                            },
                                            error = state.isShowTimeError
                                        )
                                        Spacer (modifier = Modifier.height(20.dp))
                                    }


                                    item {
                                        Text(
                                            text = "Select the Ticket Price",
                                            modifier = Modifier.padding(bottom = 5.dp)
                                        )
                                        Column {
                                            OutlinedTextField(
                                                value = state.ticketCost.toString(),
                                                onValueChange = {
                                                    val cost = it.toIntOrNull()
                                                    if (cost != null) {
                                                        onEvent(AdminAddShowEvent.UpdateTicketCost(cost))
                                                    } else {
                                                        onEvent(AdminAddShowEvent.UpdateTicketCost(0))
                                                    }
                                                },
                                                modifier = Modifier.fillMaxWidth(),
                                                shape = CircleShape,
                                                colors = TextFieldDefaults.colors().copy(
                                                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                                                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                                                    focusedIndicatorColor = Color.Gray,
                                                    unfocusedIndicatorColor = Color.Gray
                                                ),
                                                keyboardOptions = KeyboardOptions(
                                                    keyboardType = KeyboardType.Number
                                                )
                                            )
                                            if (state.isTicketCostError.isNotEmpty()) {
                                                Text(
                                                    text = state.isTicketCostError,
                                                    color = Color.Red,
                                                    modifier = Modifier.padding(
                                                        start = 20.dp, top = 10.dp
                                                    )
                                                )
                                            }
                                        }
                                    }


                                }
                            }
                        }
                    }
                }

                Spacer(Modifier.height(10.dp))

                Row(
                    modifier = Modifier.padding(horizontal = 10.dp).fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    Row(
                        modifier = Modifier.weight(1f)
                    ) {
                        if (pagerState.currentPage != 0) {
                            Box(

                            ) {
                                SubmitButton(
                                    loading = false,
                                    title = "Back",
                                    onClick = {
                                        coroutineScope.launch {
                                            val next =
                                                (pagerState.currentPage - 1).coerceAtLeast(0)
                                            lastAllowedPage = next
                                            pagerState.animateScrollToPage(
                                                next,
                                                animationSpec = tween(1000)
                                            )

                                        }
                                    }
                                )
                            }
                        }
                    }
                    Row(
                        modifier = Modifier.weight(1f)
                    ) {
                        if (pagerState.currentPage == 3) {
                            Box(
                            ) {
                                SubmitButton(
                                    loading = isCreatingShow,
                                    title = "Create",
                                    onClick = {
                                        onEvent(AdminAddShowEvent.AddShowSubmit)
                                    }
                                )
                            }
                        }
                    }

                }


            }
        }



    }

}

@Composable
fun MovieSelectCard(
    movie: Movie,
    selected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            }
            .padding(10.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Black1C1
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        ),
        border = BorderStroke(
            width = 3.dp,
            color = if (selected) RedE31 else Color.Transparent
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Black1C1)
        ) {
            // Movie Poster and Details Layout
            Column(

            ) {
                // Movie Poster
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(2 / 3f)
                        .clip(
                            RoundedCornerShape(
                                topStart = 8.dp,
                                topEnd = 8.dp,
                                bottomStart = 0.dp,
                                bottomEnd = 0.dp
                            )
                        )
                ) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(movie.Poster)
                            .placeholder(R.drawable.placeholder)
                            .error(R.drawable.placeholder)
                            .build(),
                        contentDescription = movie.Title,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )

                    // Rating Overlay
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(8.dp)
                            .background(
                                color = RedE31.copy(alpha = 0.8f),
                                shape = RoundedCornerShape(4.dp)
                            )
                            .padding(4.dp)
                    ) {
                        Text(
                            text = movie.imdbRating,
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // Movie Details
                Column(
                    modifier = Modifier
                        .padding(top = 8.dp, bottom = 10.dp)
                        .padding(horizontal = 10.dp)
                        .fillMaxWidth()
                ) {
                    // Movie Title
                    Text(
                        text = movie.Title,
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )

                    // Movie Year and Genre
                    Text(
                        text = "${movie.Year} • ${movie.Genre}",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }

}

@Composable
fun TheateSelectionrCard(theater: TheaterDetails, onClick: () -> Unit, selected: Boolean) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(8.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Black161), // Dark background
        elevation = CardDefaults.cardElevation(8.dp),
        border = BorderStroke(
            width = 3.dp,
            color = if (selected) RedE31 else Color.Transparent
        )

    ) {
        Box {
            Column {
                // Theater Image

                Box {
                    AsyncImage(
                        model = theater.image,
                        contentDescription = "Theater Image",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                            .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)),
                        contentScale = ContentScale.Crop
                    )

                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(10.dp)
                    ) {
                        Text(
                            text = theater.name,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }

                }


                // Theater Info
                Column(
                    modifier = Modifier
                        .clickable { onClick() }
                        .fillMaxWidth()
                        .padding(12.dp)
                ) {

                    TheaterDetailRow(Icons.Outlined.Place, theater.address)
                    TheaterDetailRow(
                        Icons.Outlined.Person,
                        "${theater.city}, ${theater.state} - ${theater.pincode}"
                    )
                    TheaterDetailRow(Icons.Outlined.Phone, theater.contactNumber)

                }
            }

            // Screens Count Badge
            Box(
                modifier = Modifier
                    .padding(8.dp)
                    .align(Alignment.TopEnd)
                    .background(Color.Red, shape = RoundedCornerShape(16.dp))
                    .padding(horizontal = 10.dp, vertical = 4.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(R.drawable.show),
                        contentDescription = "Screens",
                        tint = Color.White,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${theater.screens.size} Screens",
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }


        }
    }
}


@Composable
fun ScreenSelectionCard(screen: Screen, onClick: () -> Unit, selected: Boolean) {
    Card(
        modifier = Modifier
            .clickable { onClick() }
            .fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        elevation = CardDefaults.cardElevation(6.dp),
        border = BorderStroke(
            width = 3.dp,
            color = if (selected) RedE31 else Color.Transparent
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Black1C1),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = screen.screenName,
                    modifier = Modifier
                        .weight(0.8f)
                        .padding(bottom = 5.dp),
                    textAlign = TextAlign.Start,
                    style = MaterialTheme.typography.titleLarge,
                    color = RedE31
                )

            }
            HorizontalDivider()
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Row(
                    modifier = Modifier
                        .weight(0.5f)
                        .padding(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.Start
                ) {
                    Icon(
                        painter = painterResource(R.drawable.movie),
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(Modifier.width(10.dp))
                    Text(text = screen.screenType)
                }
                Row(
                    modifier = Modifier
                        .weight(0.5f)
                        .padding(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.Start
                ) {
                    Icon(
                        painter = painterResource(R.drawable.headphones),
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(Modifier.width(10.dp))
                    Text(text = screen.soundType)
                }
            }


        }


    }
}