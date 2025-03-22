package com.example.moviego.presentation.navgraph.admin_navgraph

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.key
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.moviego.presentation.admin.admin_add_movie.AdminAddMovieEvent
import com.example.moviego.presentation.admin.admin_add_movie.AdminAddMovieScreen
import com.example.moviego.presentation.admin.admin_add_movie.AdminAddMovieViewModel
import com.example.moviego.presentation.admin.admin_add_show.AdminAddShowEvent
import com.example.moviego.presentation.admin.admin_add_show.AdminAddShowScreen
import com.example.moviego.presentation.admin.admin_add_show.AdminAddShowViewModel
import com.example.moviego.presentation.admin.admin_add_theater.AdminAddTheaterEvent
import com.example.moviego.presentation.admin.admin_add_theater.AdminAddTheaterScreen
import com.example.moviego.presentation.admin.admin_add_theater.AdminAddTheaterViewModel
import com.example.moviego.presentation.admin.admin_details.AdminDetailsEvent
import com.example.moviego.presentation.admin.admin_details.AdminDetailsScreen
import com.example.moviego.presentation.admin.admin_details.AdminDetailsViewModel
import com.example.moviego.presentation.admin.admin_edit_theater.AdminEditTheaterEvent
import com.example.moviego.presentation.admin.admin_edit_theater.AdminEditTheaterScreen
import com.example.moviego.presentation.admin.admin_edit_theater.AdminEditTheaterViewModel
import com.example.moviego.presentation.admin.admin_movies.AdminMoviesEvent
import com.example.moviego.presentation.admin.admin_movies.AdminMoviesScreen
import com.example.moviego.presentation.admin.admin_movies.AdminMoviesViewModel
import com.example.moviego.presentation.admin.admin_show_details.AdminShowDetailsScreen
import com.example.moviego.presentation.admin.admin_show_details.AdminShowDetailsViewModel
import com.example.moviego.presentation.admin.admin_shows.AdminShowsScreen
import com.example.moviego.presentation.admin.admin_shows.AdminShowsViewModel
import com.example.moviego.presentation.admin.admin_theater_details.AdminTheaterDetailsEvent
import com.example.moviego.presentation.admin.admin_theater_details.AdminTheaterDetailsScreen
import com.example.moviego.presentation.admin.admin_theater_details.AdminTheaterDetailsViewModel
import com.example.moviego.presentation.admin.admin_theaters.AdminTheatersEvent
import com.example.moviego.presentation.admin.admin_theaters.AdminTheatersScreen
import com.example.moviego.presentation.admin.admin_theaters.AdminTheatersViewModel
import com.example.moviego.presentation.navgraph.Route
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
            AdminShowsScreen(
                shows = adminShowsViewModel.shows.value,
                onEvent = adminShowsViewModel::onEvent,
                navController = navController,
                selectedFilters = adminShowsViewModel.selectedFilters,
                filterOptions = adminShowsViewModel.filterOptions
            )
        }

        composable(
            route = Route.AdminShowDetails.route,
            arguments = listOf(navArgument(SHOW_ID) { type = NavType.StringType })
        ) { backStackEntry ->
            val showId = backStackEntry.arguments?.getString(SHOW_ID) ?: return@composable
            val adminShowDetailsViewModel: AdminShowDetailsViewModel = hiltViewModel()
            adminShowDetailsViewModel.loadShow(showId)
            AdminShowDetailsScreen(
                showDetails = adminShowDetailsViewModel.showDetails,
                isLoading = adminShowDetailsViewModel.isLoading,
                onEvent = adminShowDetailsViewModel::onEvent
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
                isCreatingShow = adminAddShowViewModel.isCreatingShow
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
                navController = navController
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
                navController = navController
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
                addMovieState = adminAddMovieViewModel.addMovieState,
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
    }
}