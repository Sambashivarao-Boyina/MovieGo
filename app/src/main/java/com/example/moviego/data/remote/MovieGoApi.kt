package com.example.moviego.data.remote

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
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface MovieGoApi {
    //Admin Apis
    @POST("api/admin/auth/login")
    suspend fun loginAdmin(
        @Body login: Login
    ): Response<AuthResponse>

    @POST("api/admin/auth/signup")
    suspend fun signUpAdmin(
        @Body signUp: SignUp
    ): Response<AuthResponse>

    @GET("api/admin/auth/refreshtoken")
    suspend fun refreshAdminToken():Response<AuthResponse>

    @GET("api/admin/show")
    suspend fun getAllShows():Response<List<Show>>

    @GET("api/admin/show/{showId}")
    suspend fun getShowDetails(@Path("showId") showId:String): Response<ShowDetails>

    @GET("api/admin/movie")
    suspend fun getAllAdminMovies(): Response<List<Movie>>

    @GET("api/admin/theater")
    suspend fun getAllAdminTheaters(): Response<List<TheaterDetails>>

    @POST("api/admin/show")
    suspend fun createShow(
        @Body show: NewShow
    ): Response<ResponseBody>

    //Admin Details
    @GET("api/admin")
    suspend fun getAdminDetails(): Response<Admin>

    @PATCH("api/admin/phoneNumber")
    suspend fun updatePhone(@Body data: UpdateBody): Response<Admin>

    @PATCH("api/admin/password")
    suspend fun updatePassword(@Body data:UpdateBody): Response<ResponseBody>



    //User Apis
    @POST("api/user/auth/signup")
    suspend fun signUpUser(
        @Body signUp: SignUp
    ): Response<AuthResponse>

    @POST("api/user/auth/login")
    suspend fun loginUser(
        @Body login: Login
    ): Response<AuthResponse>

    @GET("api/user/auth/refreshtoken")
    suspend fun refreshUserToken(): Response<AuthResponse>



}