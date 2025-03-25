package com.example.moviego.presentation.admin.details

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviego.data.remote.Dao.UpdateBody
import com.example.moviego.domain.model.Admin
import com.example.moviego.domain.usecases.admin_usecases.AdminUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminDetailsViewModel @Inject constructor(
    private val adminUseCases: AdminUseCases
) : ViewModel() {
    var sideEffect by mutableStateOf<String?>(null)
        private set

    var isLoading by mutableStateOf(false)
        private set

    var adminDetails by mutableStateOf<Admin?>(null)
        private set

    var changePhoneNumber by mutableStateOf(ChangePhoneNumber())
        private set

    var changePassword by mutableStateOf(ChangePassword())
        private set

    init {
        getAdminDetails()
    }

    fun onEvent(event: AdminDetailsEvent) {
        when (event) {
            AdminDetailsEvent.RemoveSideEffect -> {
                sideEffect = null
            }

            is AdminDetailsEvent.UpdatePhoneNumber -> {
                changePhoneNumber =
                    changePhoneNumber.copy(newPhoneNumber = event.phoneNumber, isError = "")
            }

            AdminDetailsEvent.SubmitNewPhoneNumber -> {
                if (isValidPhoneNumber(changePhoneNumber.newPhoneNumber)) {
                    updateNewPhoneNumber()
                } else {
                    changePhoneNumber = changePhoneNumber.copy(isError = "Enter valid phone number")
                }
            }

            is AdminDetailsEvent.UpdatePhoneNumberPopup -> {
                changePhoneNumber = changePhoneNumber.copy(isOpenPopup = event.isOpenPopup)
            }

            AdminDetailsEvent.SubmitUpdatePassword -> {
                if(isValidPassword()) {
                    updatePassword()
                }
            }
            is AdminDetailsEvent.UpdateConfirmPassword -> {
                changePassword = changePassword.copy(confirmPassword = event.confirmPassword, isConfirmPasswordError = "")
            }
            is AdminDetailsEvent.UpdateNewPassword -> {
                changePassword = changePassword.copy(newPassword = event.newPassword, isNewPasswordError = "")
            }
            is AdminDetailsEvent.UpdatePasswordPopup -> {
                changePassword = changePassword.copy(isOpenPopup = event.isOpenPopup)
            }

            AdminDetailsEvent.Logout -> {
                logoutAdmin()
            }
        }
    }

    private fun getAdminDetails() {
        viewModelScope.launch {
            isLoading = true

            val result = adminUseCases.getAdminDetails()
            if (result.isSuccess) {
                adminDetails = result.getOrNull()
            } else {
                sideEffect = result.exceptionOrNull()?.message
            }

            isLoading = false
        }

    }

    private fun isValidPhoneNumber(phoneNumber: String): Boolean {
        val regex = Regex("^[0-9]{10}$")
        return regex.matches(phoneNumber)
    }

    private fun updateNewPhoneNumber() {
        viewModelScope.launch {
            changePhoneNumber = changePhoneNumber.copy(isChangingPhone = true)
            val result = adminUseCases.updatePhoneNumber(
                data = UpdateBody(
                    changePhoneNumber.newPhoneNumber
                )
            )
            if (result.isSuccess) {
                val newAdmin = result.getOrNull()
                if (newAdmin != null) {
                    adminDetails = newAdmin
                    changePhoneNumber = ChangePhoneNumber()
                    sideEffect = "Phone number updated"
                } else {
                    sideEffect = "Something went wrong"
                }
            } else {
                sideEffect = result.exceptionOrNull()?.message
            }
            changePhoneNumber = changePhoneNumber.copy(isChangingPhone = false)
        }
    }


    private fun updatePassword() {
        val password = changePassword.newPassword.trim()

        viewModelScope.launch {
            changePassword = changePassword.copy(isUpdatingPassword = true)
            val result = adminUseCases.updatePassword(data = UpdateBody(data = password))
            if(result.isSuccess) {
                sideEffect = result.getOrElse { "Password is Updated" }
                changePassword = ChangePassword()
            } else {
                sideEffect = result.exceptionOrNull()?.message
            }
            changePassword = changePassword.copy(isUpdatingPassword = false)
        }
    }



    private fun isValidPassword(): Boolean {
        val passwordPattern = Regex("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#\$%^&+=!]).{8,}$")
        var result = true
        if(!passwordPattern.matches(changePassword.newPassword)) {
            result = false
            changePassword = changePassword.copy(isNewPasswordError = "Enter a Strong Password")
        }

        if(changePassword.newPassword != changePassword.confirmPassword) {
            result = false
            changePassword = changePassword.copy(isConfirmPasswordError = "Both password doest match")
        }

        return result
    }

    private fun logoutAdmin() {
        viewModelScope.launch {
            val result = adminUseCases.logOutAdmin()
            if(result.isSuccess) {
                sideEffect = result.getOrElse { "Logged Successfully" }
            } else {
                sideEffect = result.exceptionOrNull()?.message
            }
        }
    }

}

data class ChangePhoneNumber(
    val newPhoneNumber: String = "",
    val isError: String = "",
    val isChangingPhone: Boolean = false,
    val isOpenPopup: Boolean = false
)

data class ChangePassword(
    val newPassword: String = "",
    val confirmPassword: String = "",
    val isNewPasswordError: String = "",
    val isConfirmPasswordError: String = "",
    val isUpdatingPassword: Boolean = false,
    val isOpenPopup: Boolean = false
)

sealed class AdminDetailsEvent {
    data object RemoveSideEffect : AdminDetailsEvent()

    data class UpdatePhoneNumber(val phoneNumber: String) : AdminDetailsEvent()
    data class UpdatePhoneNumberPopup(val isOpenPopup: Boolean) : AdminDetailsEvent()
    data object SubmitNewPhoneNumber : AdminDetailsEvent()

    data class UpdateNewPassword(val newPassword: String): AdminDetailsEvent()
    data class UpdateConfirmPassword(val confirmPassword: String): AdminDetailsEvent()
    data class UpdatePasswordPopup(val isOpenPopup: Boolean): AdminDetailsEvent()
    data object SubmitUpdatePassword: AdminDetailsEvent()

    data object Logout: AdminDetailsEvent()

}