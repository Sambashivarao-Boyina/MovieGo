package com.example.moviego.presentation.navgraph

import com.example.moviego.util.Constants.SHOW_ID
import com.example.moviego.util.Constants.THEATER_ID

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
    object AdminAddShow: Route("adminAddShows")
    object AdminDetails: Route("adminDetails")

    object AdminShowDetails: Route("adminShowDetails/{${SHOW_ID}}") {
        fun passShowId(showId: String): String {
            return  this.route.replace(oldValue = "{${SHOW_ID}}", newValue = showId)
        }
    }


    object AdminMovies: Route("adminMovies")

    object AdminAddMovie: Route("adminAddMovie")

    object AdminTheaters: Route("adminTheaters")

    object AdminTheaterDetails: Route("adminTheaterDetails/{${THEATER_ID}}") {
        fun passTheaterId(theaterId: String): String {
            return this.route.replace(oldValue = "{${THEATER_ID}}", newValue = theaterId)
        }
    }

}