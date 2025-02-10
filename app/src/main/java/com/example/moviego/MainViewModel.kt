package com.example.moviego

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviego.domain.manager.LocalUserManager
import com.example.moviego.domain.usecases.admin_usecases.AdminUseCases
import com.example.moviego.domain.usecases.admin_usecases.SignUpAdminUseCase
import com.example.moviego.domain.usecases.user_usecases.UserUseCases
import com.example.moviego.presentation.navgraph.Route
import com.example.moviego.util.Constants.ADMIN
import com.example.moviego.util.Constants.USER
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val localUserManager: LocalUserManager,
    private val userUseCases: UserUseCases,
    private val adminUseCase: AdminUseCases
):ViewModel() {
    var splashCondition by mutableStateOf(true)
        private set

    var startDestination by mutableStateOf(Route.AuthRoutes.route)

    init {
        viewModelScope.launch {
            localUserManager.getUserType().collectLatest { type ->
                Log.d("step","step1")
                if(type == USER) {
                    val result = userUseCases.refreshUserToken()
                    if(result.isSuccess) {
                        startDestination = Route.UserRoutes.route
                    } else{
                        startDestination = Route.AuthRoutes.route
                    }
                } else if(type == ADMIN) {
                    val result = adminUseCase.refreshAdminToken()
                    if(result.isSuccess) {
                        startDestination = Route.AdminRoutes.route
                    } else {
                        startDestination = Route.AuthRoutes.route
                    }
                } else {
                    startDestination = Route.AuthRoutes.route
                }
                splashCondition = false
                delay(300)

            }
        }

    }
}