package com.example.moviego.domain.repository.admin

import com.example.moviego.data.remote.Dao.AuthResponse
import com.example.moviego.data.remote.Dao.Login
import com.example.moviego.data.remote.Dao.SignUp
import com.example.moviego.domain.model.Show
import retrofit2.Response

interface AdminRepository {
    suspend fun loginAdmin(admin: Login): Response<AuthResponse>
    suspend fun signUpAdmin(admin: SignUp): Response<AuthResponse>
    suspend fun refreshAdminToken(): Response<AuthResponse>
    suspend fun getAllShows():Response<List<Show>>
}