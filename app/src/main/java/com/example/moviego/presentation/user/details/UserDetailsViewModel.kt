package com.example.moviego.presentation.user.details

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviego.data.remote.Dao.UpdateBody
import com.example.moviego.domain.model.User
import com.example.moviego.domain.usecases.user_usecases.UserUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserDetailsViewModel @Inject constructor(
    private val userUserUseCases: UserUseCases
): ViewModel() {
    var sideEffect by mutableStateOf<String?>(null)
        private set

    var isLoading by mutableStateOf(false)
        private set

    var userDetails by mutableStateOf<User?>(null)
        private set

    var changePhoneNumber by mutableStateOf(ChangePhoneNumber())
        private set

    var changePassword by mutableStateOf(ChangePassword())
        private set

    init {
        getUserDetails()
    }

    fun onEvent(event: UserDetailsEvent) {
        when (event) {
            UserDetailsEvent.RemoveSideEffect -> {
                sideEffect = null
            }

            is UserDetailsEvent.UpdatePhoneNumber -> {
                changePhoneNumber =
                    changePhoneNumber.copy(newPhoneNumber = event.phoneNumber, isError = "")
            }

            UserDetailsEvent.SubmitNewPhoneNumber -> {
                if (isValidPhoneNumber(changePhoneNumber.newPhoneNumber)) {
                    updateNewPhoneNumber()
                } else {
                    changePhoneNumber = changePhoneNumber.copy(isError = "Enter valid phone number")
                }
            }

            is UserDetailsEvent.UpdatePhoneNumberPopup -> {
                changePhoneNumber = changePhoneNumber.copy(isOpenPopup = event.isOpenPopup)
            }

            UserDetailsEvent.SubmitUpdatePassword -> {
                if(isValidPassword()) {
                    updatePassword()
                }
            }
            is UserDetailsEvent.UpdateConfirmPassword -> {
                changePassword = changePassword.copy(confirmPassword = event.confirmPassword, isConfirmPasswordError = "")
            }
            is UserDetailsEvent.UpdateNewPassword -> {
                changePassword = changePassword.copy(newPassword = event.newPassword, isNewPasswordError = "")
            }
            is UserDetailsEvent.UpdatePasswordPopup -> {
                changePassword = changePassword.copy(isOpenPopup = event.isOpenPopup)
            }

            UserDetailsEvent.Logout -> {
                logoutAdmin()
            }
        }
    }

    private fun getUserDetails() {
        viewModelScope.launch {
            isLoading = true

            val result = userUserUseCases.getUserDetails()
            if (result.isSuccess) {
                userDetails = result.getOrNull()
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
            val result = userUserUseCases.updateUserPhone(
                data = UpdateBody(
                    changePhoneNumber.newPhoneNumber
                )
            )
            if (result.isSuccess) {
                val newUser = result.getOrNull()
                if (newUser != null) {
                    userDetails = newUser
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
            val result = userUserUseCases.updateUserPassword(data = UpdateBody(data = password))
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
            val result = userUserUseCases.logoutUser()
            if(result.isSuccess) {
                sideEffect = result.getOrElse { "Logged Successfully" }
            } else {
                sideEffect = result.exceptionOrNull()?.message
            }
        }
    }
}

data class  ChangePhoneNumber(
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

sealed class UserDetailsEvent {
    data object RemoveSideEffect : UserDetailsEvent()

    data class UpdatePhoneNumber(val phoneNumber: String) : UserDetailsEvent()
    data class UpdatePhoneNumberPopup(val isOpenPopup: Boolean) : UserDetailsEvent()
    data object SubmitNewPhoneNumber : UserDetailsEvent()

    data class UpdateNewPassword(val newPassword: String): UserDetailsEvent()
    data class UpdateConfirmPassword(val confirmPassword: String): UserDetailsEvent()
    data class UpdatePasswordPopup(val isOpenPopup: Boolean): UserDetailsEvent()
    data object SubmitUpdatePassword: UserDetailsEvent()

    data object Logout: UserDetailsEvent()

}