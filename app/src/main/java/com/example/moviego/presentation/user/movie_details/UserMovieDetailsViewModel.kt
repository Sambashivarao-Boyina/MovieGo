package com.example.moviego.presentation.user.movie_details

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
class UserMovieDetailsViewModel @Inject constructor(
    private val userUseCases: UserUseCases
): ViewModel() {
    var movieId by mutableStateOf("")
        private set
    var movie by mutableStateOf<Movie?>(null)
        private set
    var isLoading by mutableStateOf(false)
        private set

    var sideEffect by mutableStateOf<String?>(null)
        private set



    fun initalizeMovieId(movieId:String) {
        this.movieId = movieId
        loadMovie()
    }

    private fun loadMovie() {
        viewModelScope.launch {
            val result = userUseCases.getMovieDetails(movieId)
            if(result.isSuccess) {
                movie = result.getOrNull()
            } else {
                sideEffect = result.exceptionOrNull()?.message
            }
        }
    }

    fun onEvent(event: UserMovieDetailsEvent) {
        when(event) {
            UserMovieDetailsEvent.RemoveSideEffect -> {
                sideEffect = null
            }
        }
    }

}

sealed class UserMovieDetailsEvent {
    data object RemoveSideEffect: UserMovieDetailsEvent()
}