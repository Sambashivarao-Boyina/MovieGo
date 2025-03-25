package com.example.moviego.presentation.admin.add_show

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.moviego.domain.model.Movie
import com.example.moviego.domain.model.TheaterDetails
import com.example.moviego.presentation.authentication.components.SubmitButton
import com.example.moviego.presentation.components.DropDownItem
import com.example.moviego.presentation.components.DropDownSelect
import com.example.moviego.presentation.components.ShowDatePicker
import com.example.moviego.presentation.components.TimePicker

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AdminAddShowScreen(
    state: AdminAddShowState,
    isLoading: Boolean,
    onEvent: (AdminAddShowEvent) -> Unit,
    movies: List<Movie>,
    theaters: List<TheaterDetails>,
    isCreatingShow: Boolean
) {

    val selectedTheater by remember(state.theater, theaters) {
        onEvent(AdminAddShowEvent.UpdateScreen(""))
        derivedStateOf {
            theaters.find { it._id === state.theater }
        }
    }

    Scaffold{
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
        ) {
            Box(modifier = Modifier.weight(0.05f)) {
                Text(
                    text = "Add Show",
                    style = MaterialTheme.typography.displaySmall,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }

            LazyColumn(
                modifier = Modifier.weight(0.85f).padding(horizontal = 10.dp) ,
                verticalArrangement = Arrangement.Top
            ) {
                item {
                    Column(
                        modifier = Modifier.padding(bottom = 50.dp)
                    ) {
                        Text(text = "Select the Movie", modifier = Modifier.padding(bottom = 5.dp))
                        DropDownSelect(
                            items = movies.map { movie ->
                                DropDownItem(
                                    title = movie.title,
                                    ref = movie._id
                                )
                            },
                            onSelect = {
                                onEvent(AdminAddShowEvent.UpdateMovie(it.ref))
                            },
                            error = state.isMovieError,
                            unAvailableMessage = "No Movies Available"
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        Text(text = "Select the Theater", modifier = Modifier.padding(bottom = 5.dp))
                        DropDownSelect(
                            items = theaters.map { theater ->
                                DropDownItem(
                                    title = theater.name,
                                    ref = theater._id
                                )
                            },
                            onSelect = {
                                onEvent(AdminAddShowEvent.UpdateTheater(it.ref))
                            },
                            error = state.isTheaterError,
                            unAvailableMessage = "No Theaters Available"
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        Text(text = "Select the Screen", modifier = Modifier.padding(bottom = 5.dp))
                        DropDownSelect(
                            items = selectedTheater?.screens?.map { screen ->
                                DropDownItem(
                                    title = screen.screenName,
                                    ref = screen._id
                                )
                            } ?: emptyList(),
                            onSelect = {
                                onEvent(AdminAddShowEvent.UpdateScreen(it.ref))
                            },
                            error = state.isScreenError,
                            unAvailableMessage = "No Screens Available"
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        Text(text = "Select the Date", modifier = Modifier.padding(bottom = 5.dp))
                        ShowDatePicker(
                            selectedDate = state.date,
                            onSelected = {
                                onEvent(AdminAddShowEvent.UpdateDate(it))
                            },
                            error = state.isDateError
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        Text(text = "Select the Time", modifier = Modifier.padding(bottom = 5.dp))
                        TimePicker(
                            selectedTime = state.showTime,
                            onSelected = {
                                onEvent(AdminAddShowEvent.UpdateShowTime(it))
                            },
                            error = state.isShowTimeError
                        )

                        Spacer(modifier = Modifier.height(20.dp))
                        Text(text = "Select the Ticket Price", modifier = Modifier.padding(bottom = 5.dp))
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
                                    text = state.isTicketCostError, color = Color.Red, modifier = Modifier.padding(
                                        start = 20.dp, top = 10.dp
                                    )
                                )
                            }
                        }
                    }
                }
            }


            Box(
                modifier = Modifier.weight(0.1f)
                    .padding(10.dp)
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