package com.example.moviego.presentation.user.home

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.datastore.dataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviego.domain.manager.LocalUserManager
import com.example.moviego.domain.model.Movie
import com.example.moviego.domain.usecases.user_usecases.UserUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserHomeViewModel @Inject constructor(
    private val userUserUseCases: UserUseCases
): ViewModel() {
    var isLoading by mutableStateOf(false)
        private set

    var movies by mutableStateOf<List<Movie>>(emptyList())
        private set

    var sideEffect by mutableStateOf<String?>(null)
        private set

    var userLocation by mutableStateOf("")
        private set

    var state by mutableStateOf("")
        private set

    var city by mutableStateOf("")
        private set




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
        }
    }

    init {
        getUserLocation()
        loadMovies()
    }

    private fun loadMovies() {
        isLoading = true
        viewModelScope.launch {
            Log.d("home", userLocation)
            val state = if (userLocation == "") "empty" else userLocation.split(".")[0]
            Log.d("state", state)
            val city = if(userLocation == "") "empty" else userLocation.split(".")[1]
            Log.d("city", city)
            val result = userUserUseCases.getMovies(state = state, city = city)
            if(result.isSuccess) {
                movies = result.getOrDefault(emptyList())
            } else {
                sideEffect = result.exceptionOrNull()?.message
            }
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



}

sealed class UserHomeEvent {
    data object RemoveSideEffect: UserHomeEvent()
    data class SetUserLocation(var location: String): UserHomeEvent()
    data class SetNewState(var state: String): UserHomeEvent()
    data class SetNewCity(var city: String): UserHomeEvent()
    data object LoadMovies: UserHomeEvent()
}