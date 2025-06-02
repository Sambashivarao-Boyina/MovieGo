package com.example.moviego.domain.repository.admin

import com.example.moviego.data.remote.Dao.AuthResponse
import com.example.moviego.data.remote.Dao.Login
import com.example.moviego.data.remote.Dao.NewScreen
import com.example.moviego.data.remote.Dao.NewShow
import com.example.moviego.data.remote.Dao.SignUp
import com.example.moviego.data.remote.Dao.UpdateBody
import com.example.moviego.domain.model.Admin
import com.example.moviego.domain.model.Booking
import com.example.moviego.domain.model.BookingDetails
import com.example.moviego.domain.model.Movie
import com.example.moviego.domain.model.Show
import com.example.moviego.domain.model.ShowDetails
import com.example.moviego.domain.model.TheaterDetails
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response

interface AdminRepository {
    suspend fun loginAdmin(admin: Login): Response<AuthResponse>
    suspend fun signUpAdmin(admin: SignUp): Response<AuthResponse>
    suspend fun refreshAdminToken(): Response<AuthResponse>
    suspend fun getAllShows():Response<List<Show>>
    suspend fun getShowDetails(showId: String): Response<ShowDetails>
    suspend fun getAllAdminMovies(): Response<List<Movie>>
    suspend fun getMovieDetails(movieId: String): Response<Movie>
    suspend fun getAllAdminTheaters(): Response<List<TheaterDetails>>
    suspend fun createNewShow(newShow: NewShow): Response<ResponseBody>
    suspend fun getAdminDetails(): Response<Admin>
    suspend fun updatePhoneNumber(data: UpdateBody): Response<Admin>
    suspend fun updatePassword(data: UpdateBody): Response<ResponseBody>
    suspend fun getTheaterDetails(theaterId: String): Response<TheaterDetails>
    suspend fun addNewMovie(movieName: String): Response<ResponseBody>
    suspend fun createNewScreen(theaterId: String, newScreen: NewScreen): Response<TheaterDetails>
    suspend fun editScreen(screenId: String, editScreen: NewScreen): Response<TheaterDetails>
    suspend fun addNewTheater(image: MultipartBody.Part, newTheater: RequestBody): Response<ResponseBody>
    suspend fun editTheater(theaterId: String,image: MultipartBody.Part?, editTheater: RequestBody): Response<ResponseBody>
    suspend fun openShow(showId: String): Response<ShowDetails>
    suspend fun closeShow(showId: String): Response<ShowDetails>
    suspend fun getBookingsOfShow(showId: String): Response<List<Booking>>
}