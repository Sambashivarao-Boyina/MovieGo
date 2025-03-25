package com.example.moviego.presentation.admin.add_show

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviego.data.remote.Dao.NewShow
import com.example.moviego.domain.model.Movie
import com.example.moviego.domain.model.TheaterDetails
import com.example.moviego.domain.usecases.admin_usecases.AdminUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminAddShowViewModel @Inject constructor(
    private val adminUseCases: AdminUseCases
): ViewModel() {

    var sideEffect by mutableStateOf<String?>(null)
        private set

    var isLoading by mutableStateOf(false)
        private set

    var adminMovies by mutableStateOf<List<Movie>>(emptyList())
        private set

    var adminTheaters by mutableStateOf<List<TheaterDetails>>(emptyList())
        private set

    var state by mutableStateOf(AdminAddShowState())
        private set

    var isCreatingShow by mutableStateOf(false)
        private set


    init {
        isLoading = true
        getAllMovies()
        getAllTheaters()
        isLoading = false
    }

    fun onEvent(event: AdminAddShowEvent) {
        when(event) {
            AdminAddShowEvent.RemoveSideEffect -> {
                sideEffect = null
            }
            is AdminAddShowEvent.UpdateDate -> {
                state = state.copy(date = event.date, isDateError = "")
            }
            is AdminAddShowEvent.UpdateMovie -> {
                state = state.copy(movie = event.movie, isMovieError = "")
            }
            is AdminAddShowEvent.UpdateScreen -> {
                state = state.copy(screen = event.screen, isScreenError = "")
            }
            is AdminAddShowEvent.UpdateShowTime -> {
                state = state.copy(showTime = event.showTime, isShowTimeError = "")
            }
            is AdminAddShowEvent.UpdateTheater -> {
                state = state.copy(theater = event.theater, isTheaterError = "")
            }
            is AdminAddShowEvent.UpdateTicketCost -> {
                state = state.copy(ticketCost = event.ticketCost, isTicketCostError = "")
            }
            AdminAddShowEvent.AddShowSubmit -> {
                if(validateAddShow()) {
                    createShow()
                }
            }
        }
    }
    
    private fun validateAddShow(): Boolean {
        var result = true
        if(state.movie.isEmpty()) {
            result = false
            state = state.copy(isMovieError = "Select the Movie")
        }
        if(state.theater.isEmpty()) {
            result = false
            state = state.copy(isTheaterError = "Select the Theater")
        }
        if(state.screen.isEmpty()) {
            result = false
            state = state.copy(isScreenError = "Select the Screen")
        }
        if(state.ticketCost == 0) {
            result = false
            state = state.copy(isTicketCostError = "Enter the ticket Price")
        }
        if(state.date.isEmpty()) {
            result = false
            state = state.copy(isDateError = "Select the Date")
        }
        if(state.showTime.isEmpty()) {
            result = false
            state = state.copy(isShowTimeError = "Select the ShowTime")
        }
        
        return result
    }
    private fun getAllMovies() {
        viewModelScope.launch {
            val result = adminUseCases.getAllAdminMovies()

            if(result.isSuccess) {
                adminMovies = result.getOrDefault(emptyList())
            } else {
                sideEffect = result.exceptionOrNull()?.message
            }
        }
    }

    private fun getAllTheaters() {
        viewModelScope.launch {
            val result = adminUseCases.getAllAdminTheaters()
            if(result.isSuccess) {
                adminTheaters = result.getOrDefault(emptyList())
            } else {
                sideEffect = result.exceptionOrNull()?.message
            }
        }
    }

    private fun createShow() {
        viewModelScope.launch {
            isCreatingShow = true
            val newShow = NewShow(
                movie = state.movie,
                theater = state.theater,
                screen = state.screen,
                ticketCost = state.ticketCost,
                date = state.date,
                showTime = state.showTime
            )

            val result = adminUseCases.createNewShow(newShow)
            if(result.isSuccess) {
                sideEffect = result.getOrDefault("Movie is Created")
                state = AdminAddShowState()
            } else {
                sideEffect = result.exceptionOrNull()?.message
            }
            isCreatingShow = false
        }
    }
}

data class AdminAddShowState(
    val movie: String = "",
    val isMovieError: String = "",
    val theater: String = "",
    val isTheaterError: String = "",
    val screen : String = "",
    val isScreenError: String = "",
    val ticketCost: Int = 0,
    val isTicketCostError: String = "",
    val date: String = "",
    val isDateError: String = "",
    val showTime : String = "",
    val isShowTimeError: String = ""
)
sealed class AdminAddShowEvent {
    data object RemoveSideEffect: AdminAddShowEvent()
    data class UpdateMovie(val movie:String): AdminAddShowEvent()
    data class UpdateTheater(val theater:String): AdminAddShowEvent()
    data class UpdateScreen(val screen:String): AdminAddShowEvent()
    data class UpdateTicketCost(val ticketCost:Int): AdminAddShowEvent()
    data class UpdateDate(val date:String): AdminAddShowEvent()
    data class UpdateShowTime(val showTime:String): AdminAddShowEvent()
    data object AddShowSubmit: AdminAddShowEvent()
}