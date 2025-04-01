package com.example.moviego.presentation.navgraph.user_navgraph

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.moviego.presentation.navgraph.Route
import com.example.moviego.presentation.user.bookings.UserBookingViewModel
import com.example.moviego.presentation.user.bookings.UserBookingsScreen
import com.example.moviego.presentation.user.details.UserDetailsEvent
import com.example.moviego.presentation.user.details.UserDetailsScreen
import com.example.moviego.presentation.user.details.UserDetailsViewModel
import com.example.moviego.presentation.user.home.UserHomeEvent
import com.example.moviego.presentation.user.home.UserHomeScreen
import com.example.moviego.presentation.user.home.UserHomeViewModel
import com.example.moviego.presentation.user.movie_details.UserMovieDetailsEvent
import com.example.moviego.presentation.user.movie_details.UserMovieDetailsScreen
import com.example.moviego.presentation.user.movie_details.UserMovieDetailsViewModel
import com.example.moviego.presentation.user.movie_shows.UserMovieShowsEvent
import com.example.moviego.presentation.user.movie_shows.UserMovieShowsScreen
import com.example.moviego.presentation.user.movie_shows.UserMovieShowsViewModel
import com.example.moviego.presentation.user.show_booking.ShowBookingScreen
import com.example.moviego.presentation.user.show_booking.ShowBookingViewModel
import com.example.moviego.util.Constants.MOVIE_ID
import com.example.moviego.util.Constants.SHOW_ID

@Composable
fun UserNavGraph(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = Route.UserHomeRoute.route
    ) {
        composable(route = Route.UserHomeRoute.route) {
            val userHomeViewModel: UserHomeViewModel = hiltViewModel()
            if (userHomeViewModel.sideEffect != null) {
                Toast.makeText(
                    LocalContext.current,
                    userHomeViewModel.sideEffect,
                    Toast.LENGTH_SHORT
                ).show()
                userHomeViewModel.onEvent(UserHomeEvent.RemoveSideEffect)
            }
            UserHomeScreen(
                isLoading = userHomeViewModel.isLoading,
                movies = userHomeViewModel.movies,
                navController = navController
            )
        }

        composable(route = Route.UserDetails.route) {
            val userDetailsViewModel: UserDetailsViewModel = hiltViewModel()
            if (userDetailsViewModel.sideEffect != null) {
                Toast.makeText(
                    LocalContext.current,
                    userDetailsViewModel.sideEffect,
                    Toast.LENGTH_SHORT
                ).show()
                userDetailsViewModel.onEvent(UserDetailsEvent.RemoveSideEffect)
            }
            UserDetailsScreen(
                onEvent = userDetailsViewModel::onEvent,
                changePassword = userDetailsViewModel.changePassword,
                changePhoneNumber = userDetailsViewModel.changePhoneNumber,
                user = userDetailsViewModel.userDetails,
                navController = navController,
            )
        }

        composable(route = Route.UserBookings.route) {
            val userBookingViewModel: UserBookingViewModel = hiltViewModel()
            UserBookingsScreen()
        }

        composable(
            route = Route.UserMovieDetails.route,
            arguments = listOf(navArgument(MOVIE_ID) { type = NavType.StringType })
        ) { backStackEntry ->
            val movieId = backStackEntry.arguments?.getString(MOVIE_ID) ?: return@composable
            val userMovieDetailsViewModel: UserMovieDetailsViewModel = hiltViewModel()

            LaunchedEffect(key1 = movieId) {
                if (movieId.isNotEmpty()) {
                    userMovieDetailsViewModel.initalizeMovieId(movieId)
                }
            }

            if (userMovieDetailsViewModel.sideEffect != null) {
                Toast.makeText(
                    LocalContext.current,
                    userMovieDetailsViewModel.sideEffect,
                    Toast.LENGTH_SHORT
                ).show()
                userMovieDetailsViewModel.onEvent(UserMovieDetailsEvent.RemoveSideEffect)
            }

            UserMovieDetailsScreen(
                movie = userMovieDetailsViewModel.movie,
                isLoading = userMovieDetailsViewModel.isLoading,
                onEvent = userMovieDetailsViewModel::onEvent,
                navController = navController
            )
        }

        composable(
            route = Route.UserMovieShows.route,
            arguments = listOf(navArgument(MOVIE_ID) { type = NavType.StringType })
        ) { backStackEntry ->
            val movieId = backStackEntry.arguments?.getString(MOVIE_ID) ?: return@composable
            val userMovieShowsViewModel: UserMovieShowsViewModel = hiltViewModel()

            if (userMovieShowsViewModel.sideEffect != null) {
                Toast.makeText(
                    LocalContext.current,
                    userMovieShowsViewModel.sideEffect,
                    Toast.LENGTH_SHORT
                ).show()
                userMovieShowsViewModel.onEvent(UserMovieShowsEvent.RemoveSideEffect)
            }

            LaunchedEffect(key1 = movieId) {
                userMovieShowsViewModel.initializeMovieId(movieId)
            }

            UserMovieShowsScreen(
                isLoading = userMovieShowsViewModel.isLoading,
                shows = userMovieShowsViewModel.shows,
                onEvent = userMovieShowsViewModel::onEvent,
                movie = userMovieShowsViewModel.movie,
                navController = navController
            )
        }

        composable(
            route = Route.UserMovieShowBooking.route,
            arguments = listOf(navArgument(SHOW_ID) { type = NavType.StringType })
        ) { backStackEntry ->
            val showId = backStackEntry.arguments?.getString(SHOW_ID) ?: return@composable
            val showBookingViewModel: ShowBookingViewModel = hiltViewModel()
            LaunchedEffect(key1 = showId) {
                showBookingViewModel.initializeShowId(showId)
            }

            if(showBookingViewModel.sideEffect != null) {
                Toast.makeText(
                    LocalContext.current,
                    showBookingViewModel.sideEffect,
                    Toast.LENGTH_SHORT
                ).show()
            }

            ShowBookingScreen(
                showDetails = showBookingViewModel.showDetails,
                isLoading = showBookingViewModel.isLoading,
                onEvent = showBookingViewModel::onEvent
            )
        }
    }
}