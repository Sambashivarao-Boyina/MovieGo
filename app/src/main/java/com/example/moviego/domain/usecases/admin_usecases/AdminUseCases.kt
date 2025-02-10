package com.example.moviego.domain.usecases.admin_usecases

data class AdminUseCases(
    val loginAdminUseCase: LoginAdminUseCase,
    val signUpAdminUseCase: SignUpAdminUseCase,
    val refreshAdminToken: RefreshAdminToken
)
