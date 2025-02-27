package com.example.moviego.domain.repository.admin

import com.example.moviego.data.remote.Dao.AuthResponse
import com.example.moviego.data.remote.Dao.Login
import com.example.moviego.data.remote.Dao.NewShow
import com.example.moviego.data.remote.Dao.SignUp
import com.example.moviego.data.remote.Dao.UpdateBody
import com.example.moviego.domain.model.Admin
import com.example.moviego.domain.model.Movie
import com.example.moviego.domain.model.Show
import com.example.moviego.domain.model.ShowDetails
import com.example.moviego.domain.model.TheaterDetails
import okhttp3.ResponseBody
import retrofit2.Response

interface AdminRepository {
    suspend fun loginAdmin(admin: Login): Response<AuthResponse>
    suspend fun signUpAdmin(admin: SignUp): Response<AuthResponse>
    suspend fun refreshAdminToken(): Response<AuthResponse>
    suspend fun getAllShows():Response<List<Show>>
    suspend fun getShowDetails(showId: String): Response<ShowDetails>
    suspend fun getAllAdminMovies(): Response<List<Movie>>
    suspend fun getAllAdminTheaters(): Response<List<TheaterDetails>>
    suspend fun createNewShow(newShow: NewShow): Response<ResponseBody>
    suspend fun getAdminDetails(): Response<Admin>
    suspend fun updatePhoneNumber(data: UpdateBody): Response<Admin>
    suspend fun updatePassword(data: UpdateBody): Response<ResponseBody>
}