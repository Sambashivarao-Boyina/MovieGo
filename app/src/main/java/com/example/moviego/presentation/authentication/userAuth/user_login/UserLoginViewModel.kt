package com.example.moviego.presentation.authentication.userAuth.user_login

import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviego.data.remote.Dao.Login
import com.example.moviego.domain.usecases.user_usecases.UserUseCases
import com.example.moviego.presentation.authentication.adminAuth.admin_login.LoginState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserLoginViewModel @Inject constructor(
    private val userUseCases: UserUseCases
):ViewModel() {
    private val _state = mutableStateOf(UserLoginState())
    val state: State<UserLoginState> = _state

    var sideEffect by mutableStateOf<String?>(null)
        private set

    var isLoading by mutableStateOf(false)
        private set

    fun onEvent(event: UserLoginEvent) {
        when(event) {
            UserLoginEvent.RemoveSideEffect ->{
                sideEffect = null
            }
            UserLoginEvent.LoginUser -> {
                loginUser()
            }
            is UserLoginEvent.UpdatePassword -> {
                _state.value = _state.value.copy(password = event.password, passwordError = "")
            }
            is UserLoginEvent.UpdateEmail -> {
                _state.value = _state.value.copy(email = event.email, emailError = "")
            }
        }
    }

    private fun loginUser() {
        if(!validateData()) {
            return
        }

        viewModelScope.launch {
            isLoading = true
            val result = userUseCases.loginUserUseCase(user = Login(email = _state.value.email, password = _state.value.password))

            sideEffect = result.getOrElse { it.message ?: "Something went wrong" }

            if(result.isSuccess) {
                _state.value = UserLoginState()
            }
            isLoading = false
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
    private fun isValidEmail(email: String): Boolean {
        // Regular expression for validating an email
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\$".toRegex()
        // Match the input string against the regex
        return emailRegex.matches(email)
    }
}

data class UserLoginState(
    val email:String = "",
    val emailError:String = "",
    val password:String = "",
    val passwordError: String = ""
)

sealed class UserLoginEvent {
    object RemoveSideEffect: UserLoginEvent()
    object LoginUser: UserLoginEvent()
    data class UpdateEmail(val email:String):UserLoginEvent()
    data class UpdatePassword(val password:String):UserLoginEvent()
}