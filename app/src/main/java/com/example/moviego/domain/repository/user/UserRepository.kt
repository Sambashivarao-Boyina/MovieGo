package com.example.moviego.domain.repository.user

import com.example.moviego.data.remote.Dao.AuthResponse
import com.example.moviego.data.remote.Dao.Login
import com.example.moviego.data.remote.Dao.SignUp
import retrofit2.Response

interface UserRepository {
    suspend fun signUpUser(user:SignUp): Response<AuthResponse>
    suspend fun loginUser(user:Login):Response<AuthResponse>
    suspend fun refreshUserToken():Response<AuthResponse>
}