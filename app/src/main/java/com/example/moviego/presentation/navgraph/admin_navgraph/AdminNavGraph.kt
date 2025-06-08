package com.example.moviego.presentation.navgraph.admin_navgraph

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.moviego.presentation.admin.add_movie.AdminAddMovieEvent
import com.example.moviego.presentation.admin.add_movie.AdminAddMovieScreen
import com.example.moviego.presentation.admin.add_movie.AdminAddMovieViewModel
import com.example.moviego.presentation.admin.add_show.AdminAddShowEvent
import com.example.moviego.presentation.admin.add_show.AdminAddShowScreen
import com.example.moviego.presentation.admin.add_show.AdminAddShowViewModel
import com.example.moviego.presentation.admin.add_theater.AdminAddTheaterEvent
import com.example.moviego.presentation.admin.add_theater.AdminAddTheaterScreen
import com.example.moviego.presentation.admin.add_theater.AdminAddTheaterViewModel
import com.example.moviego.presentation.admin.details.AdminDetailsEvent
import com.example.moviego.presentation.admin.details.AdminDetailsScreen
import com.example.moviego.presentation.admin.details.AdminDetailsViewModel
import com.example.moviego.presentation.admin.edit_theater.AdminEditTheaterEvent
import com.example.moviego.presentation.admin.edit_theater.AdminEditTheaterScreen
import com.example.moviego.presentation.admin.edit_theater.AdminEditTheaterViewModel
import com.example.moviego.presentation.admin.movie_details.AdminMovieDetailsScreen
import com.example.moviego.presentation.admin.movie_details.AdminMovieDetailsViewModel
import com.example.moviego.presentation.admin.movies.AdminMoviesEvent
import com.example.moviego.presentation.admin.movies.AdminMoviesScreen
import com.example.moviego.presentation.admin.movies.AdminMoviesViewModel
import com.example.moviego.presentation.admin.show_details.AdminShowDetailsEvent
import com.example.moviego.presentation.admin.show_details.AdminShowDetailsScreen
import com.example.moviego.presentation.admin.show_details.AdminShowDetailsViewModel
import com.example.moviego.presentation.admin.shows.AdminShowsEvent
import com.example.moviego.presentation.admin.shows.AdminShowsScreen
import com.example.moviego.presentation.admin.shows.AdminShowsViewModel
import com.example.moviego.presentation.admin.theater_details.AdminTheaterDetailsEvent
import com.example.moviego.presentation.admin.theater_details.AdminTheaterDetailsScreen
import com.example.moviego.presentation.admin.theater_details.AdminTheaterDetailsViewModel
import com.example.moviego.presentation.admin.theaters.AdminTheatersEvent
import com.example.moviego.presentation.admin.theaters.AdminTheatersScreen
import com.example.moviego.presentation.admin.theaters.AdminTheatersViewModel
import com.example.moviego.presentation.navgraph.Route
import com.example.moviego.util.Constants.MOVIE_ID
import com.example.moviego.util.Constants.SHOW_ID
import com.example.moviego.util.Constants.THEATER_ID

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AdminNavGraph(
    navController: NavHostController
) {

    NavHost(
        navController = navController,
        startDestination = Route.AdminShows.route
    ) {
        composable(route = Route.AdminShows.route) {
            val adminShowsViewModel: AdminShowsViewModel = hiltViewModel()
            if (adminShowsViewModel.sideEffect != null) {
                Toast.makeText(
                    LocalContext.current,
                    adminShowsViewModel.sideEffect,
                    Toast.LENGTH_SHORT
                ).show()
                adminShowsViewModel.onEvent(AdminShowsEvent.RemoveSideEffect)
            }
            AdminShowsScreen(
                shows = adminShowsViewModel.shows.value,
                onEvent = adminShowsViewModel::onEvent,
                navController = navController,
                selectedFilters = adminShowsViewModel.selectedFilters,
                filterOptions = adminShowsViewModel.filterOptions,
                isLoading = adminShowsViewModel.isLoading
            )
        }

        composable(
            route = Route.AdminShowDetails.route,
            arguments = listOf(navArgument(SHOW_ID) { type = NavType.StringType })
        ) { backStackEntry ->
            val showId = backStackEntry.arguments?.getString(SHOW_ID) ?: return@composable
            val adminShowDetailsViewModel: AdminShowDetailsViewModel = hiltViewModel()
            LaunchedEffect(key1 = showId) {
                adminShowDetailsViewModel.initializeShowId(showId)
            }
            if (adminShowDetailsViewModel.sideEffect != null) {
                Toast.makeText(
                    LocalContext.current,
                    adminShowDetailsViewModel.sideEffect,
                    Toast.LENGTH_SHORT
                ).show()
                adminShowDetailsViewModel.onEvent(AdminShowDetailsEvent.RemoveSideEffect)
            }
            AdminShowDetailsScreen(
                showDetails = adminShowDetailsViewModel.showDetails,
                isLoading = adminShowDetailsViewModel.isLoading,
                onEvent = adminShowDetailsViewModel::onEvent,
                navController = navController,
                updatingShowStatus = adminShowDetailsViewModel.updatingShowStatus,
                isBookingsLoading = adminShowDetailsViewModel.isBookingsLoading,
                bookings = adminShowDetailsViewModel.bookings
            )
        }

        composable(route = Route.AdminAddShow.route) {
            val adminAddShowViewModel: AdminAddShowViewModel = hiltViewModel()
            if (adminAddShowViewModel.sideEffect != null) {
                Toast.makeText(
                    LocalContext.current,
                    adminAddShowViewModel.sideEffect,
                    Toast.LENGTH_SHORT
                ).show()
                adminAddShowViewModel.onEvent(AdminAddShowEvent.RemoveSideEffect)
            }

            AdminAddShowScreen(
                state = adminAddShowViewModel.state,
                isLoading = adminAddShowViewModel.isLoading,
                onEvent = adminAddShowViewModel::onEvent,
                movies = adminAddShowViewModel.adminMovies,
                theaters = adminAddShowViewModel.adminTheaters,
                isCreatingShow = adminAddShowViewModel.isCreatingShow,
                isCreatedNewShow = adminAddShowViewModel.isCreatedNewShow
            )
        }

        composable(route = Route.AdminDetails.route) {
            val adminDetailsViewModel: AdminDetailsViewModel = hiltViewModel()
            if (adminDetailsViewModel.sideEffect != null) {
                Toast.makeText(
                    LocalContext.current,
                    adminDetailsViewModel.sideEffect,
                    Toast.LENGTH_SHORT
                ).show()
                adminDetailsViewModel.onEvent(AdminDetailsEvent.RemoveSideEffect)
            }

            AdminDetailsScreen(
                admin = adminDetailsViewModel.adminDetails,
                isLoading = adminDetailsViewModel.isLoading,
                changePhoneNumber = adminDetailsViewModel.changePhoneNumber,
                changePassword = adminDetailsViewModel.changePassword,
                onEvent = adminDetailsViewModel::onEvent,
                navController = navController
            )
        }

        composable(route = Route.AdminMovies.route) {
            val adminMoviesViewModel: AdminMoviesViewModel = hiltViewModel()
            if (adminMoviesViewModel.sideEffect != null) {
                Toast.makeText(
                    LocalContext.current,
                    adminMoviesViewModel.sideEffect,
                    Toast.LENGTH_SHORT
                ).show()
                adminMoviesViewModel.onEvent(AdminMoviesEvent.RemoveSideEffect)
            }

            AdminMoviesScreen(
                movies = adminMoviesViewModel.movies,
                isLoading = adminMoviesViewModel.isLoading,
                navController = navController,
                onEvent = adminMoviesViewModel::onEvent
            )
        }

        composable(route = Route.AdminTheaters.route) {
            val adminTheatersViewModel: AdminTheatersViewModel = hiltViewModel()
            if (adminTheatersViewModel.sideEffect != null) {
                Toast.makeText(
                    LocalContext.current,
                    adminTheatersViewModel.sideEffect,
                    Toast.LENGTH_SHORT
                ).show()
                adminTheatersViewModel.onEvent(AdminTheatersEvent.RemoveSideEffect)
            }

            AdminTheatersScreen(
                theaters = adminTheatersViewModel.theaters,
                isLoading = adminTheatersViewModel.isLoading,
                navController = navController,
                onEvent = adminTheatersViewModel::onEvent
            )
        }

        composable(
            route = Route.AdminTheaterDetails.route,
            arguments = listOf(navArgument(THEATER_ID) { type = NavType.StringType })
        ) { backStackEntry ->
            val theaterId = backStackEntry.arguments?.getString(THEATER_ID) ?: return@composable
            val adminTheaterDetailsViewModel: AdminTheaterDetailsViewModel = hiltViewModel()

            if (adminTheaterDetailsViewModel.sideEffect != null) {
                Toast.makeText(
                    LocalContext.current,
                    adminTheaterDetailsViewModel.sideEffect,
                    Toast.LENGTH_SHORT
                ).show()
                adminTheaterDetailsViewModel.onEvent(AdminTheaterDetailsEvent.RemoveSideEffect)
            }

            LaunchedEffect(key1 = theaterId) {
                adminTheaterDetailsViewModel.initializeTheater(theaterId)
            }

            AdminTheaterDetailsScreen(
                theaterDetails = adminTheaterDetailsViewModel.theaterDetails,
                isLoading = adminTheaterDetailsViewModel.isLoading,
                navController = navController,
                screenState = adminTheaterDetailsViewModel.newScreenState,
                onEvent = adminTheaterDetailsViewModel::onEvent,
                editScreenState = adminTheaterDetailsViewModel.editScreenState
            )
        }

        composable(route = Route.AdminAddMovie.route) {
            val adminAddMovieViewModel: AdminAddMovieViewModel = hiltViewModel()
            LaunchedEffect(Unit) {
                adminAddMovieViewModel.eventFlow.collect { event ->
                    when (event) {
                        AdminAddMovieViewModel.UiEvent.Success -> {
                            navController.previousBackStackEntry
                                ?.savedStateHandle
                                ?.set("refresh", true)

                            navController.popBackStack()

                        }
                    }
                }
            }
            if (adminAddMovieViewModel.sideEffect != null) {
                Toast.makeText(
                    LocalContext.current,
                    adminAddMovieViewModel.sideEffect,
                    Toast.LENGTH_SHORT
                ).show()
                adminAddMovieViewModel.onEvent(AdminAddMovieEvent.RemoveSideEffect)
            }

            AdminAddMovieScreen(
                movieName = adminAddMovieViewModel.movieName,
                onEvent = adminAddMovieViewModel::onEvent,
                isLoading = adminAddMovieViewModel.isLoading,
                navController = navController
            )
        }

        composable(route = Route.AdminAddTheater.route) {
            val adminAddTheaterViewModel: AdminAddTheaterViewModel = hiltViewModel()

            if (adminAddTheaterViewModel.sideEffect != null) {
                Toast.makeText(
                    LocalContext.current,
                    adminAddTheaterViewModel.sideEffect,
                    Toast.LENGTH_SHORT
                ).show()
                adminAddTheaterViewModel.onEvent(AdminAddTheaterEvent.RemoveSideEffect)
            }

            AdminAddTheaterScreen(
                newTheaterState = adminAddTheaterViewModel.newTheaterState,
                isLoading = adminAddTheaterViewModel.isLoading,
                isSuccess = adminAddTheaterViewModel.isSuccess,
                onEvent = adminAddTheaterViewModel::onEvent,
                navController = navController
            )
        }

        composable(
            route = Route.AdminEditTheater.route,
            arguments = listOf(navArgument(THEATER_ID) { type = NavType.StringType })
        ) { backStackEntry ->
            val theaterId = backStackEntry.arguments?.getString(THEATER_ID) ?: return@composable
            val adminEditTheaterViewModel: AdminEditTheaterViewModel = hiltViewModel()


            LaunchedEffect(key1 = theaterId) {
                if(theaterId != null) {
                    adminEditTheaterViewModel.initializeTheater(theaterId)
                }
            }


            if (adminEditTheaterViewModel.sideEffect != null) {
                Toast.makeText(
                    LocalContext.current,
                    adminEditTheaterViewModel.sideEffect,
                    Toast.LENGTH_SHORT
                ).show()
                adminEditTheaterViewModel.onEvent(AdminEditTheaterEvent.RemoveSideEffect)
            }


            AdminEditTheaterScreen(
                editTheaterState = adminEditTheaterViewModel.editTheaterState,
                isLoading = adminEditTheaterViewModel.isLoading,
                onEvent = adminEditTheaterViewModel::onEvent,
                isSuccess = adminEditTheaterViewModel.isSuccess,
                navController = navController
            )
        }

        composable(
            route = Route.AdminMovieDetails.route,
            arguments = listOf(navArgument(MOVIE_ID) { type = NavType.StringType })
        ) { backStackEntry ->
            val movieId = backStackEntry.arguments?.getString(MOVIE_ID) ?: return@composable
            val adminMovieDetailsViewModel: AdminMovieDetailsViewModel = hiltViewModel()
            if (adminMovieDetailsViewModel.sideEffect != null) {
                Toast.makeText(
                    LocalContext.current,
                    adminMovieDetailsViewModel.sideEffect,
                    Toast.LENGTH_SHORT
                ).show()
                adminMovieDetailsViewModel.removeSideEffect()
            }

            LaunchedEffect(key1 = movieId) {
                adminMovieDetailsViewModel.initalizeMovie(movieId)
            }

            AdminMovieDetailsScreen(
                movie = adminMovieDetailsViewModel.movie,
                isLoading = adminMovieDetailsViewModel.isLoading,
                navController = navController,
                navigateBack = adminMovieDetailsViewModel.navigateBack
            )
        }
    }
}