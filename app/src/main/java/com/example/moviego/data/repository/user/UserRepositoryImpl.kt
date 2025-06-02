package com.example.moviego.data.repository.user

import com.example.moviego.data.remote.Dao.AuthResponse
import com.example.moviego.data.remote.Dao.BookingData
import com.example.moviego.data.remote.Dao.Login
import com.example.moviego.data.remote.Dao.SignUp
import com.example.moviego.data.remote.Dao.UpdateBody
import com.example.moviego.data.remote.MovieGoApi
import com.example.moviego.domain.model.BookingDetails
import com.example.moviego.domain.model.Movie
import com.example.moviego.domain.model.Show
import com.example.moviego.domain.model.ShowDetails
import com.example.moviego.domain.model.User
import com.example.moviego.domain.repository.user.UserRepository
import okhttp3.ResponseBody
import retrofit2.Response

class UserRepositoryImpl(
    private val movieGoApi: MovieGoApi
): UserRepository {
    override suspend fun signUpUser(user: SignUp): Response<AuthResponse> {
        return movieGoApi.signUpUser(user)
    }

    override suspend fun loginUser(user: Login): Response<AuthResponse> {
        return movieGoApi.loginUser(user)
    }

    override suspend fun refreshUserToken(): Response<AuthResponse> {
        return movieGoApi.refreshUserToken()
    }

    override suspend fun getUserDetails(): Response<User> {
        return movieGoApi.getUserDetails()
    }

    override suspend fun updateUserPhoneNumber(data: UpdateBody): Response<User> {
        return movieGoApi.updateUserPhone(data = data)
    }

    override suspend fun updateUserPassword(data: UpdateBody): Response<ResponseBody> {
        return movieGoApi.updateUserPassword(data = data)
    }

    override suspend fun getMovies(state: String, city: String): Response<List<Movie>> {
        return movieGoApi.getMovies(state = state, city = city)
    }

    override suspend fun getMovieDetails(movieId: String): Response<Movie> {
        return movieGoApi.getUserMovieDetails(movieId)
    }

    override suspend fun getMovieShows(movieId: String, state: String, city: String): Response<List<Show>> {
        return movieGoApi.getMovieShows(movieId = movieId, state = state, city = city)
    }

    override suspend fun getShowDetailsForBooking(showId: String): Response<ShowDetails> {
        return movieGoApi.getShowDetailsForBooking(showId)
    }

    override suspend fun createBooking(data: BookingData): Response<ResponseBody> {
        return movieGoApi.createBooking(data)
    }

    override suspend fun getBookingsList(): Response<List<BookingDetails>> {
        return movieGoApi.getBookingsList()
    }

    override suspend fun getBookingDetails(bookingId: String): Response<BookingDetails> {
        return movieGoApi.getBookingDetails(bookingId)
    }

    override suspend fun cancelBooking(bookingId: String): Response<ResponseBody> {
        return movieGoApi.cancelBooking(bookingId)
    }

    override suspend fun checkoutBooking( paymentId: String): Response<ResponseBody> {
        return movieGoApi.checkoutBooking( paymentId)
    }
}