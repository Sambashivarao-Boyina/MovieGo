package com.example.moviego.presentation.admin.shows

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



    fun onEvent(event: AdminShowsEvent) {
        when(event) {
            AdminShowsEvent.RemoveSideEffect -> {
                sideEffect = null
            }
            is AdminShowsEvent.UpdateFilterDate -> {
                selectedFilters = selectedFilters.copy(date = event.date)
                applyFilters()
            }
            is AdminShowsEvent.UpdateFilterMovie -> {
                selectedFilters = selectedFilters.copy(movie = event.movie)
                applyFilters()
            }
            is AdminShowsEvent.UpdateFilterTheater -> {
                selectedFilters = selectedFilters.copy(theater = event.theater)
                applyFilters()
            }
            is AdminShowsEvent.UpdateShowType -> {
                selectedFilters = selectedFilters.copy(showType = event.showType)
                applyFilters()
            }
            AdminShowsEvent.ReloadShows -> {
                loadAllShows()

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
            applyFilters()
        }
    }

    private fun applyFilters() {
        shows.value = allShows.value.filter { show ->
            (selectedFilters.date.isEmpty() || selectedFilters.date == show.date) &&
                    (selectedFilters.movie == null || selectedFilters.movie!!._id == show.movie._id) &&
                    (selectedFilters.theater == null || selectedFilters.theater!!._id == show.theater._id) &&
                    (selectedFilters.showType == "All" || selectedFilters.showType == show.bookingStatus)
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
    val date: String = "",
    val showType: String = "All"
)

data class FilterOptions (
    val movies: List<Movie> = emptyList(),
    val theaters : List<Theater> = emptyList(),
    val showTypes: List<String> = listOf("All","Open","Closed")
)

sealed class AdminShowsEvent {
    data object RemoveSideEffect: AdminShowsEvent()
    data class UpdateFilterDate(val date: String) : AdminShowsEvent()
    data class UpdateFilterMovie(val movie: Movie?): AdminShowsEvent()
    data class UpdateFilterTheater(val theater: Theater?): AdminShowsEvent()
    data class UpdateShowType(val showType: String): AdminShowsEvent()
    data object ReloadShows: AdminShowsEvent()
}

