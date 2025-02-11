package com.example.moviego.presentation.admin.admin_shows

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviego.domain.model.Show
import com.example.moviego.domain.usecases.admin_usecases.AdminUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminShowsViewModel @Inject constructor(
    private val adminUseCases: AdminUseCases
) :ViewModel() {
    private val _shows = mutableStateOf<List<Show>>(emptyList())
    val shows:State<List<Show>> = _shows

    var sideEffect by mutableStateOf<String?>(null)
        private set

    var isLoading by mutableStateOf(false)
        private set

    fun onEvent(event: AdminShowEvent) {
        when(event) {
            AdminShowEvent.RemoveSideEffect -> {
                sideEffect = null
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
                _shows.value = result.getOrDefault(emptyList())
            } else {
                sideEffect = result.exceptionOrNull()?.message
            }
        }
    }
}

sealed class AdminShowEvent {
    data object RemoveSideEffect: AdminShowEvent()
}