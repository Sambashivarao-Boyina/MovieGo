package com.example.moviego.data.remote

import com.example.moviego.data.remote.Dao.AuthResponse
import com.example.moviego.data.remote.Dao.Login
import com.example.moviego.data.remote.Dao.SignUp
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

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