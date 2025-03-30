package com.example.moviego.presentation.admin.add_movie

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviego.data.remote.Dao.NewMovie
import com.example.moviego.domain.usecases.admin_usecases.AdminUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class AdminAddMovieViewModel @Inject constructor(
    private val adminUseCases: AdminUseCases
): ViewModel() {

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()
    var sideEffect by mutableStateOf<String?>(null)
        private set

    var isLoading by mutableStateOf(false)
        private set

    var movieName by mutableStateOf("")
        private set

    fun onEvent(event: AdminAddMovieEvent) {
        when(event) {
            AdminAddMovieEvent.SubmitMovie -> {
                if(movieName.isNotEmpty()) {
                    subMitMovie()
                }
            }
            AdminAddMovieEvent.RemoveSideEffect -> {
                sideEffect = null
            }

            is AdminAddMovieEvent.UpdateTitle -> {
                movieName = event.title
            }
        }
    }


    private fun subMitMovie() {
        viewModelScope.launch {
            isLoading = true
            val result = adminUseCases.addNewMovie(
                movieName = movieName
            )
            if(result.isSuccess) {
                sideEffect = result.getOrElse { "Movie Created Successfully" }
                _eventFlow.emit(UiEvent.Success)
            } else {
                sideEffect = result.exceptionOrNull()?.message
            }
            isLoading = false
        }
    }

    sealed class UiEvent {
        data object Success : UiEvent()
    }

}



sealed class AdminAddMovieEvent {
    data class UpdateTitle(var title: String): AdminAddMovieEvent()
    data object SubmitMovie: AdminAddMovieEvent()
    data object RemoveSideEffect: AdminAddMovieEvent()
}