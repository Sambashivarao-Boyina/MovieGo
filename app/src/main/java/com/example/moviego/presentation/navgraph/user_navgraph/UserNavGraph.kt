package com.example.moviego.presentation.navgraph.user_navgraph

import android.util.Log
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
import com.example.moviego.presentation.user.booking_details.BookingDetailsEvent
import com.example.moviego.presentation.user.booking_details.BookingDetailsScreen
import com.example.moviego.presentation.user.booking_details.BookingDetailsViewModel
import com.example.moviego.presentation.user.bookings.UserBookingViewModel
import com.example.moviego.presentation.user.bookings.UserBookingsEvent
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
import com.example.moviego.presentation.user.payment_confirmation.UserPaymentConfirmationEvent
import com.example.moviego.presentation.user.payment_confirmation.UserPaymentConfirmationScreen
import com.example.moviego.presentation.user.payment_confirmation.UserPaymentConfirmationViewModel
import com.example.moviego.presentation.user.show_booking.ShowBookingEvent
import com.example.moviego.presentation.user.show_booking.ShowBookingScreen
import com.example.moviego.presentation.user.show_booking.ShowBookingViewModel
import com.example.moviego.util.Constants.BOOKING_ID
import com.example.moviego.util.Constants.MOVIE_ID
import com.example.moviego.util.Constants.SHOW_ID

@Composable
fun UserNavGraph(
    navController: NavHostController,
    userPaymentConfirmationViewModel: UserPaymentConfirmationViewModel
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
                movies = userHomeViewModel.movies.value,
                navController = navController,
                location = userHomeViewModel.userLocation,
                onEvent = userHomeViewModel::onEvent,
                state = userHomeViewModel.state,
                selectedGenres = userHomeViewModel.selectedGenres,
                genres = userHomeViewModel.movieGerners,
                selectedLanguage = userHomeViewModel.selectedLanguage,
                languages = userHomeViewModel.movieLanguages
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
                isLoading = userDetailsViewModel.isLoading
            )
        }

        composable(route = Route.UserBookings.route) {
            val userBookingsViewModel: UserBookingViewModel = hiltViewModel()

            if(userBookingsViewModel.sideEffect != null) {
                Toast.makeText(
                    LocalContext.current,
                    userBookingsViewModel.sideEffect,
                    Toast.LENGTH_SHORT
                ).show()
                userBookingsViewModel.onEvent(UserBookingsEvent.RemoveSideEffect)
            }

            UserBookingsScreen(
                isLoading = userBookingsViewModel.isLoading,
                bookingDetails = userBookingsViewModel.bookingDetails,
                onEvent = userBookingsViewModel::onEvent,
                navController = navController
            )
        }

        composable(
            route = Route.UserBookingDetails.route,
            arguments = listOf(navArgument(BOOKING_ID) {type = NavType.StringType})
        ) { backStackEntry ->
            val bookingId = backStackEntry.arguments?.getString(BOOKING_ID) ?: return@composable
            val bookingDetailsViewModel: BookingDetailsViewModel = hiltViewModel()

            LaunchedEffect(key1 = bookingId) {
                if(bookingId.isNotEmpty()) {
                    bookingDetailsViewModel.onEvent(BookingDetailsEvent.InitializeBookingId(bookingId))
                }
            }

            if(bookingDetailsViewModel.sideEffect != null) {
                Toast.makeText(
                    LocalContext.current,
                    bookingDetailsViewModel.sideEffect,
                    Toast.LENGTH_SHORT
                ).show()
                bookingDetailsViewModel.onEvent(BookingDetailsEvent.RemoveSideEffect)
            }

            BookingDetailsScreen(
                isLoading = bookingDetailsViewModel.isLoading,
                bookingDetails = bookingDetailsViewModel.bookingDetails,
                navController = navController
            )
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
                showBookingViewModel.onEvent(ShowBookingEvent.RemoveSideEffect)
            }

            ShowBookingScreen(
                showDetails = showBookingViewModel.showDetails,
                isLoading = showBookingViewModel.isLoading,
                onEvent = showBookingViewModel::onEvent,
                navController = navController,
                seatSelectionLimit = showBookingViewModel.seatSelectionLimit,
                selectedSeats = showBookingViewModel.selectedSeats,
                isCreatingBooking = showBookingViewModel.isCreatingBooking,
                bookingId = showBookingViewModel.bookingId
            )
        }


        composable(
            route = Route.UserPaymentConfirmation.route,
            arguments = listOf(navArgument(BOOKING_ID) {type = NavType.StringType})
        ) { backStackEntry ->
            val bookingId = backStackEntry.arguments?.getString(BOOKING_ID) ?: return@composable
            if(userPaymentConfirmationViewModel.sideEffect != null) {
                Log.d("sideEffect", userPaymentConfirmationViewModel.sideEffect.toString())
                Toast.makeText(
                    LocalContext.current,
                    userPaymentConfirmationViewModel.sideEffect,
                    Toast.LENGTH_SHORT
                ).show()
                userPaymentConfirmationViewModel.onEvent(UserPaymentConfirmationEvent.RemoveSideEffect)
            }

            LaunchedEffect(key1 = bookingId) {
                userPaymentConfirmationViewModel.initializeBookingId(bookingId)
            }

            UserPaymentConfirmationScreen(
                navController = navController,
                isLoading = userPaymentConfirmationViewModel.isLoading,
                bookingDetails = userPaymentConfirmationViewModel.bookingDetails,
                onEvent = userPaymentConfirmationViewModel::onEvent,
                cancelingBooking = userPaymentConfirmationViewModel.cancelingBooking,
                navigateBack = userPaymentConfirmationViewModel.navigateBack,
                ticketsPrice = userPaymentConfirmationViewModel.ticketsPrice,
                convenienceFees = userPaymentConfirmationViewModel.convenienceFees,
                totalPayment = userPaymentConfirmationViewModel.totalPayment,
                paymentState = userPaymentConfirmationViewModel.paymentState
            )
        }
    }
}