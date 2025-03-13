package com.example.moviego.presentation.admin.admin_add_theater

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.moviego.domain.usecases.admin_usecases.AdminUseCases
import com.example.moviego.presentation.admin.admin_theater_details.AdminTheaterDetailsEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import java.io.File
import javax.inject.Inject

@HiltViewModel
class AdminAddTheaterViewModel @Inject constructor(
    private val adminUseCases: AdminUseCases
) : ViewModel(){
    var newTheaterState by mutableStateOf(NewTheaterState())
        private set

    var sideEffect by mutableStateOf<String?>(null)
        private set

    var isLoading by mutableStateOf(false)
        private set

    fun onEvent(event: AdminAddTheaterEvent) {
        when(event) {
            AdminAddTheaterEvent.RemoveSideEffect -> {
                sideEffect = null
            }
            is AdminAddTheaterEvent.UpdateAddress -> {
                newTheaterState = newTheaterState.copy(addressError = "", address = event.address)
            }
            is AdminAddTheaterEvent.UpdateCity -> {
                newTheaterState = newTheaterState.copy(cityError = "", city = event.city)
            }
            is AdminAddTheaterEvent.UpdateContactNumber -> {
                newTheaterState = newTheaterState.copy(contactNumberError = "", contactNumber = event.contactNumber)
            }
            is AdminAddTheaterEvent.UpdateImage -> {
                newTheaterState = newTheaterState.copy(imageError = "", image = event.image)
            }
            is AdminAddTheaterEvent.UpdateName -> {
                newTheaterState = newTheaterState.copy(nameError = "", name = event.name)
            }
            is AdminAddTheaterEvent.UpdatePincode -> {
                newTheaterState = newTheaterState.copy(pincodeError = "", pincode = event.pincode)
            }
            is AdminAddTheaterEvent.UpdateState -> {
                newTheaterState = newTheaterState.copy(stateError = "", state = event.state)
            }
        }
    }
}

data class NewTheaterState(
    val name: String = "",
    val nameError: String = "",
    val image: File? = null,
    val imageError: String = "",
    val city: String = "",
    val cityError: String = "",
    val contactNumber: String = "",
    val contactNumberError: String = "",
    val address: String = "",
    val addressError: String = "",
    val state: String = "",
    val stateError: String = "",
    val pincode: String = "",
    val pincodeError: String = ""
)

sealed class AdminAddTheaterEvent{
    data object RemoveSideEffect: AdminAddTheaterEvent()
    data class UpdateName(val name: String): AdminAddTheaterEvent()
    data class UpdateImage(val image: File): AdminAddTheaterEvent()
    data class UpdateCity(val city: String): AdminAddTheaterEvent()
    data class UpdateContactNumber(val contactNumber: String): AdminAddTheaterEvent()
    data class UpdateAddress(val address: String): AdminAddTheaterEvent()
    data class UpdateState(val state: String): AdminAddTheaterEvent()
    data class UpdatePincode(val pincode: String): AdminAddTheaterEvent()
}