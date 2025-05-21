package com.example.moviego.presentation.admin.add_theater

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviego.data.remote.Dao.NewTheater
import com.example.moviego.domain.usecases.admin_usecases.AdminUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class AdminAddTheaterViewModel @Inject constructor(
    private val adminUseCases: AdminUseCases
) : ViewModel() {
    var newTheaterState by mutableStateOf(NewTheaterState())
        private set

    var isSuccess by mutableStateOf(false)
        private set


    var sideEffect by mutableStateOf<String?>(null)
        private set

    var isLoading by mutableStateOf(false)
        private set

    fun onEvent(event: AdminAddTheaterEvent) {
        when (event) {
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
                newTheaterState = newTheaterState.copy(
                    contactNumberError = "",
                    contactNumber = event.contactNumber
                )
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

            AdminAddTheaterEvent.SubmitNewTheater -> {
                if (validateNewTheater()) {
                    submitNewTheater()
                }
            }

            is AdminAddTheaterEvent.UpdateCoordinates -> {
                newTheaterState = newTheaterState.copy(longitude = event.longitude, latitude = event.latitude, cooridateError = "")
            }
        }
    }

    private fun submitNewTheater() {
        viewModelScope.launch {
            isLoading = true
            val result = adminUseCases.addNewTheater(
                newTheater = NewTheater(
                    name = newTheaterState.name,
                    address = newTheaterState.address,
                    contactNumber = newTheaterState.contactNumber,
                    city = newTheaterState.city,
                    state = newTheaterState.state,
                    pincode = newTheaterState.pincode,
                    longitude = newTheaterState.longitude,
                    latitude = newTheaterState.latitude
                ),
                image = newTheaterState.image!!
            )

            if(result.isSuccess) {
                sideEffect = "New Theater is Added"
                isSuccess = true
            } else {
                sideEffect = result.exceptionOrNull()?.message
            }
            isLoading = false
        }
    }

    private fun validateNewTheater(): Boolean {
        var result = true
        if (newTheaterState.name.isEmpty()) {
            result = false
            newTheaterState = newTheaterState.copy(nameError = "Name is Required")
        }
        if (newTheaterState.image == null) {
            result = false
            newTheaterState = newTheaterState.copy(imageError = "Select an Image")
        }
        if (newTheaterState.city.isEmpty()) {
            result = false
            newTheaterState = newTheaterState.copy(cityError = "City is Required")
        }
        if (newTheaterState.contactNumber.isEmpty() || newTheaterState.contactNumber.length != 10) {
            result = false
            newTheaterState = newTheaterState.copy(contactNumberError = "Enter Valid Contact Number")
        }
        if (newTheaterState.address.isEmpty()) {
            result = false
            newTheaterState = newTheaterState.copy(addressError = "Address is Required")
        }
        if (newTheaterState.state.isEmpty()) {
            result = false
            newTheaterState = newTheaterState.copy(stateError = "State is Required")
        }
        if (newTheaterState.pincode.isEmpty() || newTheaterState.pincode.length != 6) {
            result = false
            newTheaterState = newTheaterState.copy(pincodeError = "Enter Valid Pincode")
        }
        if(newTheaterState.latitude == 0.0 || newTheaterState.longitude == 0.0) {
            result = false
            newTheaterState = newTheaterState.copy(cooridateError = "Coordinates are Required")
        }


        return result
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
    val pincodeError: String = "",
    val longitude: Double = 0.0,
    val latitude: Double = 0.0,
    val cooridateError: String = ""
)

sealed class AdminAddTheaterEvent {
    data object RemoveSideEffect : AdminAddTheaterEvent()
    data class UpdateName(val name: String) : AdminAddTheaterEvent()
    data class UpdateImage(val image: File) : AdminAddTheaterEvent()
    data class UpdateCity(val city: String) : AdminAddTheaterEvent()
    data class UpdateContactNumber(val contactNumber: String) : AdminAddTheaterEvent()
    data class UpdateAddress(val address: String) : AdminAddTheaterEvent()
    data class UpdateState(val state: String) : AdminAddTheaterEvent()
    data class UpdatePincode(val pincode: String) : AdminAddTheaterEvent()
    data object SubmitNewTheater : AdminAddTheaterEvent()
    data class UpdateCoordinates(val longitude: Double, val latitude: Double): AdminAddTheaterEvent()
}