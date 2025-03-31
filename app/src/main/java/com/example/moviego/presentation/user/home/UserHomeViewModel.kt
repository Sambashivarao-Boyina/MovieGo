package com.example.moviego.presentation.user.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

    var movies by mutableStateOf<List<Movie>>(emptyList())
        private set

    var sideEffect by mutableStateOf<String?>(null)
        private set

    fun onEvent(event: UserHomeEvent) {
        when(event) {
            UserHomeEvent.RemoveSideEffect -> {
                sideEffect = null
            }
        }
    }

    init {
        loadMovies()
    }

    private fun loadMovies() {
        isLoading = true
        viewModelScope.launch {
            val result = userUserUseCases.getMovies()
            if(result.isSuccess) {
                movies = result.getOrDefault(emptyList())
            } else {
                sideEffect = result.exceptionOrNull()?.message
            }
        }
        isLoading = false
    }

}

sealed class UserHomeEvent {
    data object RemoveSideEffect: UserHomeEvent()
}