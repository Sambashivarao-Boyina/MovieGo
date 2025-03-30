package com.example.moviego.presentation.admin.movie_details

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviego.domain.model.Movie
import com.example.moviego.domain.usecases.admin_usecases.AdminUseCases
import com.example.moviego.domain.usecases.user_usecases.UserUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminMovieDetailsViewModel @Inject constructor(
    private val adminUseCases: AdminUseCases
): ViewModel() {
    var isLoading by mutableStateOf(false)
        private set

    var movie by mutableStateOf<Movie?>(null)
        private set

    var sideEffect by mutableStateOf<String?>(null)
        private set

    var navigateBack by mutableStateOf(false)
        private set

    fun removeSideEffect() {
        sideEffect = null
    }

    fun initalizeMovie(movieId: String) {
        isLoading = true
        viewModelScope.launch {
            val result = adminUseCases.getMovieDetails(movieId)
            if(result.isSuccess) {
                movie = result.getOrNull()
                if(movie === null) {
                    sideEffect = "Something went wrong"
                    navigateBack = true
                }
            }else {
                sideEffect = result.exceptionOrNull()?.message
                navigateBack = true
            }
        }
        isLoading = false
    }


}