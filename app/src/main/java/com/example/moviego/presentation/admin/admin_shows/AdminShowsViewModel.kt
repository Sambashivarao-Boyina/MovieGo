package com.example.moviego.presentation.admin.admin_shows

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviego.domain.model.Movie
import com.example.moviego.domain.model.Show
import com.example.moviego.domain.model.Theater
import com.example.moviego.domain.usecases.admin_usecases.AdminUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminShowsViewModel @Inject constructor(
    private val adminUseCases: AdminUseCases
) :ViewModel() {
    private val allShows = mutableStateOf<List<Show>>(emptyList())
    val shows = mutableStateOf<List<Show>>(emptyList())

    var sideEffect by mutableStateOf<String?>(null)
        private set

    var isLoading by mutableStateOf(false)
        private set

    var selectedFilters by mutableStateOf(SelectedFilters())
    var filterOptions by mutableStateOf(FilterOptions())



    fun onEvent(event: AdminShowEvent) {
        when(event) {
            AdminShowEvent.RemoveSideEffect -> {
                sideEffect = null
            }
            is AdminShowEvent.UpdateFilterDate -> {
                selectedFilters = selectedFilters.copy(date = event.date)
                applyFilters()
            }
            is AdminShowEvent.UpdateFilterMovie -> {
                selectedFilters = selectedFilters.copy(movie = event.movie)
                applyFilters()
            }
            is AdminShowEvent.UpdateFilterTheater -> {
                selectedFilters = selectedFilters.copy(theater = event.theater)
                applyFilters()
            }
        }
    }

    init {
        loadAllShows()
    }



    private fun loadAllShows() {
        viewModelScope.launch {
            val result = adminUseCases.getAllShows();
            if(result.isSuccess) {
                allShows.value = result.getOrDefault(emptyList())
                shows.value = result.getOrDefault(emptyList())
            } else {
                sideEffect = result.exceptionOrNull()?.message
            }
            extractFilterOptions()
        }
    }

    private fun applyFilters() {
        shows.value = allShows.value.filter { show ->
            (selectedFilters.date.isEmpty() || selectedFilters.date == show.date) &&
                    (selectedFilters.movie == null || selectedFilters.movie!!._id == show.movie._id) &&
                    (selectedFilters.theater == null || selectedFilters.theater!!._id == show.theater._id)
        }
    }

    private fun extractFilterOptions() {
        if(allShows.value.isNotEmpty()) {
            val uniqueMovies: MutableSet<Movie> = mutableSetOf()
            val uniqueTheaters: MutableSet<Theater> = mutableSetOf()

            for( show in allShows.value) {
                uniqueTheaters.add(show.theater)
                uniqueMovies.add(show.movie)
            }

            filterOptions = filterOptions.copy(theaters = uniqueTheaters.toList(), movies = uniqueMovies.toList())

        }
    }
}


data class SelectedFilters(
    val movie: Movie? =  null,
    val theater: Theater? = null,
    val date: String = ""
)

data class FilterOptions (
    val movies: List<Movie> = emptyList(),
    val theaters : List<Theater> = emptyList()
)

sealed class AdminShowEvent {
    data object RemoveSideEffect: AdminShowEvent()
    data class UpdateFilterDate(val date: String) : AdminShowEvent()
    data class UpdateFilterMovie(val movie: Movie?): AdminShowEvent()
    data class UpdateFilterTheater(val theater: Theater?): AdminShowEvent()
}