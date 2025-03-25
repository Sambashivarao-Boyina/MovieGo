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

    var addMovieState by mutableStateOf(MovieState())
        private set

    fun onEvent(event: AdminAddMovieEvent) {
        when(event) {
            is AdminAddMovieEvent.UpdateDuration -> {
                addMovieState = addMovieState.copy(duration = event.duration, isDurationError = "")
            }
            is AdminAddMovieEvent.UpdateLanguage -> {
                addMovieState = addMovieState.copy(language = event.language, isLanguageError = "")
            }
            is AdminAddMovieEvent.UpdateTitle -> {
                addMovieState = addMovieState.copy(title = event.title, isTitleError = "")
            }
            AdminAddMovieEvent.SubmitMovie -> {
                if(isValidMovie()) {
                    subMitMovie()
                }
            }
            is AdminAddMovieEvent.UpdatePosterImage ->  {
                addMovieState = addMovieState.copy(posterImage = event.file, isPosterImageError = "")
            }
            AdminAddMovieEvent.RemoveSideEffect -> {
                sideEffect = null
            }
        }
    }

    private fun isValidMovie():Boolean {
        var result = true
        if(addMovieState.posterImage == null) {
            result = false
            addMovieState = addMovieState.copy(isPosterImageError = "Please Select a Image")
        }
        if(addMovieState.title.isEmpty()) {
            result = false
            addMovieState = addMovieState.copy(isTitleError = "Title is Required")
        }
        if(addMovieState.duration == 0) {
            result = false
            addMovieState = addMovieState.copy(isDurationError = "Enter Duration")
        }

        if(addMovieState.language.isEmpty()) {
            result = false
            addMovieState = addMovieState.copy(isLanguageError = "Language is Required")
        }

        return result
    }

    private fun subMitMovie() {
        viewModelScope.launch {
            isLoading = true
            val result = adminUseCases.addNewMovie(
                movie = NewMovie(
                    title = addMovieState.title,
                    language = addMovieState.language,
                    duration = addMovieState.duration
                ),
                poster = addMovieState.posterImage!!
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

data class MovieState(
    val title: String = "",
    val isTitleError: String = "",
    val duration: Int = 0,
    val isDurationError: String = "",
    val language: String = "",
    val isLanguageError: String = "",
    val posterImage: File? = null,
    val isPosterImageError: String = ""
)


sealed class AdminAddMovieEvent {
    data class UpdateTitle(var title: String): AdminAddMovieEvent()
    data class UpdateDuration(var duration: Int): AdminAddMovieEvent()
    data class UpdateLanguage(var language: String): AdminAddMovieEvent()
    data class UpdatePosterImage(var file: File): AdminAddMovieEvent()
    data object SubmitMovie: AdminAddMovieEvent()
    data object RemoveSideEffect: AdminAddMovieEvent()
}