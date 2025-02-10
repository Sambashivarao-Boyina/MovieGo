package com.example.moviego.presentation.authentication.adminAuth.admin_login

import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviego.data.remote.Dao.Login
import com.example.moviego.domain.usecases.admin_usecases.AdminUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminLoginViewModel @Inject constructor(
    private val adminUseCases: AdminUseCases
): ViewModel(){

    private val _state = mutableStateOf(LoginState())
    val state: State<LoginState> = _state

    var sideEffect by mutableStateOf<String?>(null)
        private set

    var isLoading by mutableStateOf<Boolean>(false)
        private set

    fun onEvent(adminLoginEvent: AdminLoginEvent) {
        when(adminLoginEvent) {
            AdminLoginEvent.RemoveSideEffect -> {
                sideEffect = null
            }
            AdminLoginEvent.Login -> {
                viewModelScope.launch {
                    LoginAdmin()
                }
            }
            is AdminLoginEvent.EmailUpdate -> {
                _state.value = _state.value.copy(email = adminLoginEvent.email, emailError = "")
            }
            is AdminLoginEvent.PasswordUpdate -> {
                _state.value = _state.value.copy(password = adminLoginEvent.password, passwordError = "")
            }
        }
    }

    private fun validateData():Boolean {
        if(_state.value.email.trim().isEmpty()) {
            _state.value = _state.value.copy(emailError = "Email is Required")
            return false
        } else if(_state.value.password.trim().isEmpty()) {
            _state.value = _state.value.copy(passwordError = "Password is Required")
            return false
        } else if(!isValidEmail(_state.value.email)) {
            _state.value = _state.value.copy(emailError = "Enter a valid email")
            return false
        }

        return true
    }

    private fun LoginAdmin() {
        if(!validateData()) {
            return
        }
        viewModelScope.launch {
            isLoading = true
            val result = adminUseCases.loginAdminUseCase(admin = Login(email = _state.value.email, password = _state.value.password))

            sideEffect = result.getOrElse { it.message ?: "Something went wrong" }

            if(result.isSuccess) {
                _state.value = LoginState()
            }
            isLoading = false
        }
    }

    private fun isValidEmail(email: String): Boolean {
        // Regular expression for validating an email
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\$".toRegex()
        // Match the input string against the regex
        return emailRegex.matches(email)
    }



}

data class LoginState(
    val email:String = "",
    val emailError: String = "",
    val password: String = "",
    val passwordError: String = "",
)

sealed class AdminLoginEvent {
    object RemoveSideEffect:AdminLoginEvent()
    object Login: AdminLoginEvent()
    data class EmailUpdate(val email: String): AdminLoginEvent()
    data class PasswordUpdate(val password: String): AdminLoginEvent()

}