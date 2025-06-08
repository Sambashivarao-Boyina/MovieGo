package com.example.moviego.presentation.admin.movies

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviego.domain.model.Movie
import com.example.moviego.domain.usecases.admin_usecases.AdminUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminMoviesViewModel @Inject constructor(
    private val adminUseCases: AdminUseCases
): ViewModel() {
    var movies by mutableStateOf<List<Movie>>(emptyList())
        private set
    var isLoading by mutableStateOf(false)
        private set
    var sideEffect by mutableStateOf<String?>(null)
        private set

    init {
        getAllMovies()
    }

    fun onEvent(event: AdminMoviesEvent) {
        when(event) {
            AdminMoviesEvent.RemoveSideEffect -> {
                sideEffect = null
            }
            AdminMoviesEvent.ReloadMovies -> {
                getAllMovies()
            }
        }
    }

    private fun getAllMovies() {
        viewModelScope.launch {
            isLoading = true
            val result = adminUseCases.getAllAdminMovies()
            if(result.isSuccess) {
                movies = result.getOrElse { emptyList() }
            } else {
                sideEffect = result.exceptionOrNull()?.message
            }
            isLoading = false
        }
    }
}

sealed class AdminMoviesEvent {
    data object RemoveSideEffect: AdminMoviesEvent()
    data object ReloadMovies: AdminMoviesEvent()
}