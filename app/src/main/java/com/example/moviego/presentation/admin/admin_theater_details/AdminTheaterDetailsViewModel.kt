package com.example.moviego.presentation.admin.admin_theater_details


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviego.data.remote.Dao.NewScreen
import com.example.moviego.domain.model.TheaterDetails
import com.example.moviego.domain.usecases.admin_usecases.AdminUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminTheaterDetailsViewModel @Inject constructor(
    private val adminUseCases: AdminUseCases
): ViewModel() {
    var theaterId by mutableStateOf<String?>(null)
        private set

    var theaterDetails by mutableStateOf<TheaterDetails?>(null)
        private set

    var sideEffect by mutableStateOf<String?>(null)
        private set

    var isLoading by mutableStateOf(false)
        private set

    var newScreenState by mutableStateOf(ScreenState())
        private set

    var editScreenId by mutableStateOf<String?>(null)
        private set

    var editScreenState by mutableStateOf(ScreenState())
        private set

    fun initializeTheater(theaterId: String) {
        this.theaterId = theaterId
        loadTheater()
    }

    fun onEvent(event:AdminTheaterDetailsEvent) {
        when(event) {
            AdminTheaterDetailsEvent.SubmitScreen -> {
                if(validateScreen()) {
                    theaterId?.let {
                        submitNewScreen()
                    }
                }
            }
            is AdminTheaterDetailsEvent.UpdateScreenName -> {
                newScreenState = newScreenState.copy(screenName = event.screenName, isScreenNameError = "")
            }
            is AdminTheaterDetailsEvent.UpdateScreenType -> {
                newScreenState = newScreenState.copy(screenType = event.screenType, isScreenTypeError = "")
            }
            is AdminTheaterDetailsEvent.UpdateSoundType -> {
                newScreenState = newScreenState.copy(soundType = event.soundType, isSoundTypeError = "")
            }
            is AdminTheaterDetailsEvent.UpdateShowNewScreen -> {
                newScreenState = newScreenState.copy(showNewScreen = event.showScreen)
            }

            AdminTheaterDetailsEvent.RemoveSideEffect -> {
                sideEffect = null
            }

            AdminTheaterDetailsEvent.SubmitEditScreen -> {
                if(validateEditScreen()) {
                    submitEditScreen()
                }
            }
            is AdminTheaterDetailsEvent.UpdateEditScreenName -> {
                editScreenState = editScreenState.copy(screenName = event.screenName, isScreenNameError = "")
            }
            is AdminTheaterDetailsEvent.UpdateEditScreenType -> {
                editScreenState = editScreenState.copy(screenType = event.screenType, isScreenTypeError = "")
            }
            is AdminTheaterDetailsEvent.UpdateEditSoundType -> {
                editScreenState = editScreenState.copy(soundType = event.soundType, isSoundTypeError = "")
            }
            is AdminTheaterDetailsEvent.UpdateShowEditScreen -> {
                editScreenState = editScreenState.copy(showNewScreen = event.showScreen)
            }

            is AdminTheaterDetailsEvent.SetEditScreenId -> {
                editScreenId = event.screenId
            }
        }
    }

    private fun validateScreen(): Boolean {
        var result = true

        if(newScreenState.screenName.isEmpty()) {
            result = false
            newScreenState = newScreenState.copy(isScreenNameError = "Screen Name required")
        }
        if(newScreenState.screenType.isEmpty()) {
            result = false
            newScreenState = newScreenState.copy(isScreenTypeError = "Screen Type required")
        }
        if(newScreenState.soundType.isEmpty()) {
            result = false
            newScreenState = newScreenState.copy(isSoundTypeError = "Sound Type required")
        }

        return result

    }

    private fun validateEditScreen(): Boolean {
        var result = true

        if(editScreenState.screenName.isEmpty()) {
            result = false
            editScreenState = editScreenState.copy(isScreenNameError = "Screen Name required")
        }
        if(editScreenState.screenType.isEmpty()) {
            result = false
            editScreenState = editScreenState.copy(isScreenTypeError = "Screen Type required")
        }
        if(editScreenState.soundType.isEmpty()) {
            result = false
            editScreenState = editScreenState.copy(isSoundTypeError = "Sound Type required")
        }

        return result

    }

    private fun submitEditScreen() {
        viewModelScope.launch {
            editScreenState = editScreenState.copy(submittingScreen = true)
            editScreenId?.let {
                val result = adminUseCases.editScreen(screenId = editScreenId!!, editScreen = NewScreen(
                    screenName = editScreenState.screenName,
                    screenType = editScreenState.screenType,
                    soundType = editScreenState.soundType
                ))
                if(result.isSuccess) {
                    sideEffect = "Screen is Updated"
                    theaterDetails = result.getOrNull()
                    editScreenState = ScreenState()
                    editScreenId = null
                } else {
                    sideEffect = result.exceptionOrNull()?.message
                }
            }
            editScreenState = editScreenState.copy(submittingScreen = false)
            editScreenState = editScreenState.copy(showNewScreen = false)

        }
    }
    private fun submitNewScreen() {
        viewModelScope.launch {
            newScreenState = newScreenState.copy(submittingScreen = true)
            theaterId?.let {
                val result = adminUseCases.addNewScreen(theaterId = theaterId!!, newScreen = NewScreen(
                    screenName = newScreenState.screenName,
                    screenType = newScreenState.screenType,
                    soundType = newScreenState.soundType
                ))
                if(result.isSuccess) {
                    sideEffect = "Screen is Created"
                    theaterDetails = result.getOrNull()
                    newScreenState = ScreenState()
                } else {
                    sideEffect = result.exceptionOrNull()?.message
                }
            }
            newScreenState = newScreenState.copy(submittingScreen = false)
            newScreenState = newScreenState.copy(showNewScreen = false)

        }
    }

    private fun loadTheater() {
        viewModelScope.launch {
            isLoading = true
            theaterId?.let {
                val result = adminUseCases.getTheaterDetails(theaterId!!)
                if(result.isSuccess) {
                    theaterDetails = result.getOrNull()
                } else {
                    sideEffect = result.exceptionOrNull()?.message
                }
            }
            isLoading = false
        }
    }


}

data class ScreenState(
    val showNewScreen: Boolean = false,
    val screenName: String = "",
    val isScreenNameError: String = "",
    val screenType: String = "",
    val isScreenTypeError: String = "",
    val soundType: String = "",
    val isSoundTypeError: String = "",
    val submittingScreen: Boolean = false
)

sealed class AdminTheaterDetailsEvent {
    data class UpdateShowNewScreen(val showScreen:Boolean): AdminTheaterDetailsEvent()
    data class UpdateScreenName(val screenName: String): AdminTheaterDetailsEvent()
    data class UpdateScreenType(val screenType: String): AdminTheaterDetailsEvent()
    data class UpdateSoundType(val soundType: String): AdminTheaterDetailsEvent()
    data object SubmitScreen: AdminTheaterDetailsEvent()

    //EditScreen

    data class SetEditScreenId(val screenId: String): AdminTheaterDetailsEvent()

    data class UpdateShowEditScreen(val showScreen:Boolean): AdminTheaterDetailsEvent()
    data class UpdateEditScreenName(val screenName: String): AdminTheaterDetailsEvent()
    data class UpdateEditScreenType(val screenType: String): AdminTheaterDetailsEvent()
    data class UpdateEditSoundType(val soundType: String): AdminTheaterDetailsEvent()
    data object SubmitEditScreen: AdminTheaterDetailsEvent()

    data object RemoveSideEffect: AdminTheaterDetailsEvent()
}