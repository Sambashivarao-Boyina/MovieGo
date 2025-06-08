package com.example.moviego.presentation.user.home

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.mutableStateSetOf
import androidx.compose.runtime.setValue
import androidx.datastore.dataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviego.domain.manager.LocalUserManager
import com.example.moviego.domain.model.Movie
import com.example.moviego.domain.usecases.user_usecases.UserUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserHomeViewModel @Inject constructor(
    private val userUserUseCases: UserUseCases
): ViewModel() {
    var isLoading by mutableStateOf(false)
        private set

    private val allMovies = mutableStateOf<List<Movie>>(emptyList())
    val movies = mutableStateOf<List<Movie>>(emptyList())

    var sideEffect by mutableStateOf<String?>(null)
        private set

    var userLocation by mutableStateOf("")
        private set

    var state by mutableStateOf("")
        private set

    var city by mutableStateOf("")
        private set

    var selectedGenres by mutableStateOf(setOf<String>())
        private set

    var movieGerners = listOf(
        "Action",
        "Adventure",
        "Comedy",
        "Drama",
        "Romance",
        "Horror",
        "Thriller",
        "Mystery",
        "Sci-Fi",
        "Fantasy",
        "Animation",
        "Family",
        "Musical",
        "Biography",
        "Historical",
        "Crime",
        "Documentary",
        "War",
        "Western",
        "Sport",
        "Psychological",
        "Tragedy",
        "Superhero",
        "Experimental",
        "Short Film"
    )



    fun onEvent(event: UserHomeEvent) {
        when(event) {
            UserHomeEvent.RemoveSideEffect -> {
                sideEffect = null
            }
            is UserHomeEvent.SetUserLocation -> {
                setUpUserLocation(event.location)
            }
            is UserHomeEvent.SetNewCity -> {
                city = event.city
                setUpUserLocation("$state.$city")
                getUserLocation()
            }
            is UserHomeEvent.SetNewState -> {
                state = event.state

            }
            UserHomeEvent.LoadMovies -> {
                loadMovies()
            }
            is UserHomeEvent.AddGenreFilter -> {
                selectedGenres = selectedGenres + event.genre
                applyFilters()
            }
            is UserHomeEvent.RemoveGenreFilter -> {
                selectedGenres = selectedGenres - event.genre
                applyFilters()
            }
            UserHomeEvent.ClearFilters -> {
                selectedGenres = emptySet()
                applyFilters()
            }
        }
    }

    init {
        getUserLocation()
        loadMovies()
    }

    private fun loadMovies() {
        isLoading = true
        viewModelScope.launch {
            val state = if (userLocation == "") "empty" else userLocation.split(".")[0]
            val city = if(userLocation == "") "empty" else userLocation.split(".")[1]
            val result = userUserUseCases.getMovies(state = state, city = city)
            if(result.isSuccess) {
                allMovies.value = result.getOrDefault(emptyList())
                movies.value = allMovies.value
            } else {
                sideEffect = result.exceptionOrNull()?.message
            }
            applyFilters()
        }
        isLoading = false
    }


    private fun setUpUserLocation(location: String) {
        viewModelScope.launch {
            val result = userUserUseCases.setLocation(location)
            sideEffect = if(result.isSuccess) {
                result.getOrNull()
            } else {
                result.exceptionOrNull()?.message
            }
        }
    }

    private fun getUserLocation() {
        viewModelScope.launch {
            val result = userUserUseCases.getLocation()
            if(result.isSuccess) {
                userLocation = result.getOrDefault("")
            } else {
                sideEffect = result.exceptionOrNull()?.message
            }
        }
    }


    private fun applyFilters() {
        if (selectedGenres.isNotEmpty()) {
            movies.value = allMovies.value.filter { movie ->
                movie.Genre.split(",").any { genre ->
                    selectedGenres.contains(genre.trim())
                }
            }
        } else {
            movies.value = allMovies.value
        }

    }
}

sealed class UserHomeEvent {
    data object RemoveSideEffect: UserHomeEvent()
    data class SetUserLocation(var location: String): UserHomeEvent()
    data class SetNewState(var state: String): UserHomeEvent()
    data class SetNewCity(var city: String): UserHomeEvent()
    data object LoadMovies: UserHomeEvent()
    data class AddGenreFilter(var genre: String): UserHomeEvent()
    data class RemoveGenreFilter(var genre: String): UserHomeEvent()
    data object ClearFilters: UserHomeEvent()
}