package com.example.moviego.presentation.user.movie_shows

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviego.domain.model.Movie
import com.example.moviego.domain.model.Screen
import com.example.moviego.domain.model.Show
import com.example.moviego.domain.model.Theater
import com.example.moviego.domain.usecases.user_usecases.UserUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserMovieShowsViewModel @Inject constructor(
    private val userUseCases: UserUseCases
): ViewModel() {
    var movieId by mutableStateOf("")
        private set
    var shows by mutableStateOf<Map<String,Map<Theater, Map<Screen, List<Show>>>>>(emptyMap())
        private set

    var isLoading by mutableStateOf(false)
        private set

    var sideEffect by mutableStateOf<String?>(null)
        private set

    var movie by mutableStateOf<Movie?>(null)
        private set



    fun onEvent(event: UserMovieShowsEvent) {
        when(event) {
            UserMovieShowsEvent.RemoveSideEffect -> {
                sideEffect = null
            }
        }
    }

    fun initializeMovieId(movieId: String) {
        this.movieId = movieId
        loadShows()
    }

    private fun loadShows() {
        isLoading = true
        viewModelScope.launch {
            val result = userUseCases.getMovieShows(movieId)
            if(result.isSuccess) {
                val newShows: List<Show> = result.getOrDefault(emptyList())
                if(newShows.isNotEmpty()) {
                    movie = newShows[0].movie
                    val groups:Map<String,Map<Theater, Map<Screen, List<Show>>>> = newShows.groupBy { it.date }
                        .mapValues { (_, dateShows) ->
                            dateShows.groupBy { it.theater }
                                .mapValues { (_, theaterShows) ->
                                    theaterShows.groupBy { it.screen }
                                }
                        }



                    Log.d("shows", groups.toString())
                    shows = groups
                }
            } else {
                sideEffect = result.exceptionOrNull()?.message
            }
        }
        isLoading = false
    }
}

sealed class UserMovieShowsEvent {
    data object RemoveSideEffect: UserMovieShowsEvent()
}
