package com.example.moviego.presentation.admin.add_movie

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.moviego.R
import com.example.moviego.presentation.admin.components.TopBar
import com.example.moviego.presentation.authentication.components.InputBox
import com.example.moviego.presentation.authentication.components.SubmitButton

@Composable
fun AdminAddMovieScreen(
    movieName: String,
    isLoading: Boolean,
    onEvent: (AdminAddMovieEvent) -> Unit,
    navController: NavHostController
) {

    Scaffold (
        topBar = {
            TopBar(
                title = "Add Movie",
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

        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(it).padding(horizontal = 10.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            item {
                Spacer(modifier = Modifier.height(10.dp))
                InputBox(
                    value = movieName,
                    onChange = {
                        onEvent(AdminAddMovieEvent.UpdateTitle(it))
                    },
                    placeHolder = "Enter Movie Full Name",
                    keyboardType = KeyboardType.Text,
                    leadingIcon = {
                        Icon(painter = painterResource(R.drawable.movie), contentDescription = "movie", modifier = Modifier.size(25.dp))
                    },
                )
            }


            item{
                SubmitButton(
                    title = "Add",
                    loading = isLoading,
                    onClick = {
                        onEvent(AdminAddMovieEvent.SubmitMovie)
                    }
                )
            }
        }
    }


}

