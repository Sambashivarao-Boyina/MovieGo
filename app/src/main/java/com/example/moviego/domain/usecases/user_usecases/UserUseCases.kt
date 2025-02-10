package com.example.moviego.domain.usecases.user_usecases

data class UserUseCases(
    val loginUserUseCase: LoginUserUseCase,
    val signUpUserUseCase: SignUpUserUseCase,
    val refreshUserToken: RefreshUserToken
)