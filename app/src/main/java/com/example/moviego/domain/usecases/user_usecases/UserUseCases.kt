package com.example.moviego.domain.usecases.user_usecases


data class UserUseCases(
    val loginUserUseCase: LoginUserUseCase,
    val signUpUserUseCase: SignUpUserUseCase,
    val refreshUserToken: RefreshUserToken,
    val getUserDetails: GetUserDetails,
    val updateUserPhone: UpdateUserPhone,
    val updateUserPassword: UpdateUserPassword,
    val logoutUser: LogoutUser,
    val getMovies: GetMovies,
    val getMovieDetails: GetUserMovieDetails,
    val getMovieShows: GetMovieShows,
    val getShowDetailsForBooking: GetShowDetailsForBooking,
    val createBooking: CreateBooking,
    val getBookingDetails: GetBookingDetails,
    val cancelBooking: CancelBooking,
    val checkoutBooking: CheckoutBooking
)