package com.example.moviego.domain.repository.user

import com.example.moviego.data.remote.Dao.AuthResponse
import com.example.moviego.data.remote.Dao.Login
import com.example.moviego.data.remote.Dao.SignUp
import com.example.moviego.data.remote.Dao.UpdateBody
import com.example.moviego.domain.model.Movie
import com.example.moviego.domain.model.Show
import com.example.moviego.domain.model.User
import okhttp3.ResponseBody
import retrofit2.Response

interface UserRepository {
    suspend fun signUpUser(user:SignUp): Response<AuthResponse>
    suspend fun loginUser(user:Login):Response<AuthResponse>
    suspend fun refreshUserToken():Response<AuthResponse>
    suspend fun getUserDetails():Response<User>
    suspend fun updateUserPhoneNumber(data: UpdateBody): Response<User>
    suspend fun updateUserPassword(data: UpdateBody): Response<ResponseBody>
    suspend fun getMovies(): Response<List<Movie>>
    suspend fun getMovieDetails(movieId: String): Response<Movie>
    suspend fun getMovieShows(movieId: String): Response<List<Show>>
}