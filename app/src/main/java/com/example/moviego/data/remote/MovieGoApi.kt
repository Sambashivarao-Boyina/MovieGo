package com.example.moviego.data.remote

import com.example.moviego.data.remote.Dao.AuthResponse
import com.example.moviego.data.remote.Dao.BookingData
import com.example.moviego.data.remote.Dao.Login
import com.example.moviego.data.remote.Dao.NewScreen
import com.example.moviego.data.remote.Dao.NewShow
import com.example.moviego.data.remote.Dao.SignUp
import com.example.moviego.data.remote.Dao.UpdateBody
import com.example.moviego.domain.model.Admin
import com.example.moviego.domain.model.Booking
import com.example.moviego.domain.model.Movie
import com.example.moviego.domain.model.Show
import com.example.moviego.domain.model.ShowDetails
import com.example.moviego.domain.model.TheaterDetails
import com.example.moviego.domain.model.User
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
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
    suspend fun refreshAdminToken(): Response<AuthResponse>

    @GET("api/admin/show")
    suspend fun getAllShows(): Response<List<Show>>

    @GET("api/admin/show/{showId}")
    suspend fun getShowDetails(@Path("showId") showId: String): Response<ShowDetails>

    @GET("api/admin/movie")
    suspend fun getAllAdminMovies(): Response<List<Movie>>

    @GET("api/admin/movie/{movieId}")
    suspend fun getMovieDetails(@Path("movieId") movieId: String):Response<Movie>

    @POST("api/admin/movie/{movieName}")
    suspend fun addNewMovie(
        @Path("movieName") movieName:String
    ): Response<ResponseBody>

    @Multipart
    @POST("api/admin/theater")
    suspend fun addNewTheater(
        @Part image: MultipartBody.Part,
        @Part("theater") theater:RequestBody
    ): Response<ResponseBody>

    @Multipart
    @PUT("api/admin/theater/{theaterId}")
    suspend fun editTheater(
        @Path("theaterId") theaterId: String,
        @Part image: MultipartBody.Part?,
        @Part("theater") theater: RequestBody
    ): Response<ResponseBody>


    @GET("api/admin/theater")
    suspend fun getAllAdminTheaters(): Response<List<TheaterDetails>>

    @GET("api/admin/theater/{theaterId}")
    suspend fun getTheaterDetails(@Path("theaterId") theaterId: String): Response<TheaterDetails>

    @POST("api/admin/screen/{theaterId}")
    suspend fun createNewScreen(
        @Path("theaterId") theaterId: String,
        @Body newScreen: NewScreen
    ): Response<TheaterDetails>

    @PUT("api/admin/screen/{screenId}")
    suspend fun editScreen(
        @Path("screenId") screenId: String,
        @Body editScreen: NewScreen
    ): Response<TheaterDetails>

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
    suspend fun updatePassword(@Body data: UpdateBody): Response<ResponseBody>


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

    //User Details
    @GET("api/user")
    suspend fun getUserDetails(): Response<User>

    @PATCH("api/user/phoneNumber")
    suspend fun updateUserPhone(@Body data: UpdateBody): Response<User>

    @PATCH("api/user/password")
    suspend fun updateUserPassword(@Body data: UpdateBody): Response<ResponseBody>

    @GET("api/user/movie")
    suspend fun getMovies(): Response<List<Movie>>

    @GET("api/user/movie/{movieId}")
    suspend fun getUserMovieDetails(
        @Path("movieId") movieId: String
    ): Response<Movie>

    @GET("api/user/show/{movieId}")
    suspend fun getMovieShows(
        @Path("movieId") movieId: String
    ): Response<List<Show>>

    @GET("api/user/show/{showId}/details")
    suspend fun getShowDetailsForBooking(
        @Path("showId") showId: String
    ): Response<ShowDetails>

    @POST("api/user/booking")
    suspend fun createBooking(
        @Body data:BookingData
    ): Response<Booking>

}