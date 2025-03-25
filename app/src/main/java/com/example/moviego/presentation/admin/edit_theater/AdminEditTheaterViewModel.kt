package com.example.moviego.presentation.admin.edit_theater

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
class AdminEditTheaterViewModel @Inject constructor(
    private val adminUseCases: AdminUseCases
): ViewModel() {
    
    var editTheaterState by mutableStateOf(EditTheaterState())
        private set
    
    var theaterId by mutableStateOf<String?>(null)
        private set

    var sideEffect by mutableStateOf<String?>(null)
        private set

    var isSuccess by mutableStateOf(false)
        private set

    var isLoading by mutableStateOf(false)
        private set

    fun initializeTheater(theaterId: String) {
        this.theaterId = theaterId
        loadTheater()
    }

    fun onEvent(event: AdminEditTheaterEvent) {
        when (event) {
            AdminEditTheaterEvent.RemoveSideEffect -> {
                sideEffect = null
            }

            is AdminEditTheaterEvent.UpdateAddress -> {
                editTheaterState = editTheaterState.copy(addressError = "", address = event.address)
            }

            is AdminEditTheaterEvent.UpdateCity -> {
                editTheaterState = editTheaterState.copy(cityError = "", city = event.city)
            }

            is AdminEditTheaterEvent.UpdateContactNumber -> {
                editTheaterState = editTheaterState.copy(
                    contactNumberError = "",
                    contactNumber = event.contactNumber
                )
            }

            is AdminEditTheaterEvent.UpdateImage -> {
                editTheaterState = editTheaterState.copy(imageError = "", image = event.image)
            }

            is AdminEditTheaterEvent.UpdateName -> {
                editTheaterState = editTheaterState.copy(nameError = "", name = event.name)
            }

            is AdminEditTheaterEvent.UpdatePincode -> {
                editTheaterState = editTheaterState.copy(pincodeError = "", pincode = event.pincode)
            }

            is AdminEditTheaterEvent.UpdateState -> {
                editTheaterState = editTheaterState.copy(stateError = "", state = event.state)
            }

            AdminEditTheaterEvent.SubmitEditTheater -> {
                if (validateEditTheater()) {
                    submitEditTheater()
                }
            }
        }
    }

    private fun validateEditTheater(): Boolean {
        var result = true
        if (editTheaterState.name.isEmpty()) {
            result = false
            editTheaterState = editTheaterState.copy(nameError = "Name is Required")
        }
        if (editTheaterState.city.isEmpty()) {
            result = false
            editTheaterState = editTheaterState.copy(cityError = "City is Required")
        }
        if (editTheaterState.contactNumber.isEmpty() || editTheaterState.contactNumber.length != 10) {
            result = false
            editTheaterState = editTheaterState.copy(contactNumberError = "Enter Valid Contact Number")
        }
        if (editTheaterState.address.isEmpty()) {
            result = false
            editTheaterState = editTheaterState.copy(addressError = "Address is Required")
        }
        if (editTheaterState.state.isEmpty()) {
            result = false
            editTheaterState = editTheaterState.copy(stateError = "State is Required")
        }
        if (editTheaterState.pincode.isEmpty() || editTheaterState.pincode.length != 6) {
            result = false
            editTheaterState = editTheaterState.copy(pincodeError = "Enter Valid Pincode")
        }


        return result
    }
    
    private fun submitEditTheater() {
        viewModelScope.launch {
            isLoading = true
            val result = adminUseCases.editTheater(
                editTheater = NewTheater(
                    name = editTheaterState.name,
                    address = editTheaterState.address,
                    contactNumber = editTheaterState.contactNumber,
                    city = editTheaterState.city,
                    state = editTheaterState.state,
                    pincode = editTheaterState.pincode,
                ),
                image = editTheaterState.image,
                theaterId = theaterId!!
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

    private fun loadTheater() {
        viewModelScope.launch {
            isLoading = true
            theaterId?.let {
                val result = adminUseCases.getTheaterDetails(theaterId!!)
                if(result.isSuccess) {
                    val theater = result.getOrNull()
                    theater?.let {
                        editTheaterState = editTheaterState.copy(
                            name = theater.name,
                            city = theater.city,
                            contactNumber = theater.contactNumber,
                            address = theater.address,
                            state = theater.state,
                            pincode = theater.pincode
                        )
                    }
                } else {
                    sideEffect = result.exceptionOrNull()?.message
                }
            }
            isLoading = false
        }
    }
}

data class EditTheaterState(
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

sealed class AdminEditTheaterEvent {
    data object RemoveSideEffect : AdminEditTheaterEvent()
    data class UpdateName(val name: String) : AdminEditTheaterEvent()
    data class UpdateImage(val image: File) : AdminEditTheaterEvent()
    data class UpdateCity(val city: String) : AdminEditTheaterEvent()
    data class UpdateContactNumber(val contactNumber: String) : AdminEditTheaterEvent()
    data class UpdateAddress(val address: String) : AdminEditTheaterEvent()
    data class UpdateState(val state: String) : AdminEditTheaterEvent()
    data class UpdatePincode(val pincode: String) : AdminEditTheaterEvent()
    data object SubmitEditTheater : AdminEditTheaterEvent()
}