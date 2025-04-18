package com.example.moviego.presentation.authentication.adminAuth.admin_signup

import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviego.data.remote.Dao.SignUp
import com.example.moviego.domain.usecases.admin_usecases.AdminUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminSignUpViewmodel @Inject constructor(
    private val adminUseCases: AdminUseCases
): ViewModel() {
    private val _state = mutableStateOf(AdminSignUpState())
    val state : State<AdminSignUpState> = _state

    var sideEffect by mutableStateOf<String?>(null)
        private set

    var isLoading by mutableStateOf<Boolean>(false)
        private set

     fun onEvent(event: AdminSignUpEvent) {
        when(event) {
            AdminSignUpEvent.RemoveSideEffect -> {
                sideEffect = null
            }
            AdminSignUpEvent.SignUp -> {
                viewModelScope.launch {
                    SignUpAdmin()
                }
            }
            is AdminSignUpEvent.UpdateConfirmPassword -> {
                _state.value = _state.value.copy(confirmPassword = event.confirmPassword, confirmPasswordError = "")
            }
            is AdminSignUpEvent.UpdateEmail -> {
                _state.value = _state.value.copy(email = event.email, emailError = "")
            }
            is AdminSignUpEvent.UpdateName -> {
                _state.value = _state.value.copy(name = event.name, nameError = "")
            }
            is AdminSignUpEvent.UpdatePassword -> {
                _state.value = _state.value.copy(password = event.password, passwordError = "")
            }
            is AdminSignUpEvent.UpdatePhone -> {
                _state.value = _state.value.copy(phone = event.phone, phoneError = "")
            }
        }
    }

    private fun SignUpAdmin() {
        if(!validateSignUp()) {
            return
        }

        viewModelScope.launch {
            isLoading = true

            val result = adminUseCases.signUpAdminUseCase(admin = SignUp(
                email = _state.value.email,
                name = _state.value.name,
                phone = _state.value.phone,
                password = _state.value.password
            )
            )

            sideEffect = result.getOrElse { it.message ?: "Something went wrong" }

            if(result.isSuccess) {
                _state.value = AdminSignUpState()
            }

            isLoading = false
        }


    }

    private fun validateSignUp():Boolean {
        var result = true
        if(_state.value.email.trim().isEmpty()) {
            _state.value = _state.value.copy(emailError = "Email is required")
            result = false
        }else if(!isValidEmail(_state.value.email)){
            _state.value = _state.value.copy(emailError = "Enter a valid Email")
            result =  false
        }

        if(_state.value.name.trim().isEmpty()) {
            _state.value = _state.value.copy(nameError = "Name is required")
            result = false
        }else if(_state.value.name.trim().length < 4) {
            _state.value = _state.value.copy(nameError = "Name should be at least 4 characters")
            result = false
        } else if(_state.value.name.trim().length > 20) {
            _state.value = _state.value.copy(nameError = "Name should not exceed 20 characters")
            result = false
        }
        if(_state.value.phone.trim().isEmpty()) {
            _state.value = _state.value.copy(phoneError = "Phone Number required")
            result = false
        } else if(!isValidPhone(_state.value.phone)) {
            _state.value = _state.value.copy(phoneError = "Enter a valid phone number")
            result = false
        }


        if(!isValidPassword(_state.value.password)) {
            _state.value = _state.value.copy(passwordError = "Enter a strong password")
            result = false
        }

        if(!_state.value.password.equals(_state.value.confirmPassword)) {
            _state.value = _state.value.copy(confirmPasswordError = "Both Passwords should match")
            result = false
        }

        return result
    }


    private fun isValidPhone(phone: String): Boolean {
        val phoneRegex = "^[0-9]{10}\$".toRegex()
        return phoneRegex.matches(phone)
    }
    private fun isValidEmail(email: String): Boolean {
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\$".toRegex()
        return emailRegex.matches(email)
    }
    private fun isValidPassword(password: String): Boolean {
        val passwordPattern = Regex("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#\$%^&+=!]).{8,}$")
        return password.matches(passwordPattern)
    }
}

data class AdminSignUpState(
    val name: String = "",
    val nameError: String = "",
    val email: String = "",
    val emailError: String = "",
    val phone: String = "",
    val phoneError: String = "",
    val password: String = "",
    val passwordError: String = "",
    val confirmPassword: String = "",
    val confirmPasswordError: String = ""
)

sealed class AdminSignUpEvent{
    object RemoveSideEffect: AdminSignUpEvent()
    object SignUp: AdminSignUpEvent()
    data class UpdateEmail(val email:String) : AdminSignUpEvent()
    data class UpdateName(val name:String) : AdminSignUpEvent()
    data class UpdatePhone(val phone:String) : AdminSignUpEvent()
    data class UpdatePassword(val password:String) : AdminSignUpEvent()
    data class UpdateConfirmPassword(val confirmPassword:String) : AdminSignUpEvent()
}