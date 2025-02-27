package com.example.moviego.domain.usecases.admin_usecases

data class AdminUseCases(
    val loginAdminUseCase: LoginAdminUseCase,
    val signUpAdminUseCase: SignUpAdminUseCase,
    val refreshAdminToken: RefreshAdminToken,
    val getAllShows: GetAllShows,
    val getShowDetails: GetShowDetails,
    val getAllAdminMovies: GetAllAdminMovies,
    val getAllAdminTheaters: GetAllAdminTheaters,
    val createNewShow: CreateNewShow,
    val getAdminDetails: GetAdminDetails,
    val updatePhoneNumber: UpdatePhoneNumber,
    val updatePassword: UpdatePassword,
    val logOutAdmin: LogOutAdmin
)
