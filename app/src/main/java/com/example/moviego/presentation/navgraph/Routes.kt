package com.example.moviego.presentation.navgraph

sealed class Route(val route: String) {

    //Auth routes
    object AuthRoutes:Route("authRouter")
    object AuthNavigatorScreen: Route("authNavigatorScreen")

    object AuthOptionScreen: Route("authOptionsScreen")
    object UserSignUpScreen: Route("userSignUpScreen")
    object UserLoginScreen: Route("userLoginScreen")

    object AdminSignUpScreen: Route("adminSignUpScreen")
    object AdminLoginScreen: Route("adminLoginScreen")

    //User Routes
    object UserRoutes: Route("userRoutes")
    object UserNavigatorScreen: Route("userNavigatorScreen")
    object UserHomeRoute: Route("userHomeRoute")



    //Admin Routes
    object AdminRoutes: Route("adminRoutes")
    object AdminNavigatorScreen: Route("adminNavigatorScreen")

    object AdminShows: Route("adminShows")
    object AdminManagement: Route("adminManagement")
    object AdminDetails: Route("adminDetails")


}