package com.example.moviego.data.repository.admin

import com.example.moviego.data.remote.Dao.AuthResponse
import com.example.moviego.data.remote.Dao.Login
import com.example.moviego.data.remote.Dao.SignUp
import com.example.moviego.data.remote.MovieGoApi
import com.example.moviego.domain.repository.admin.AdminRepository
import retrofit2.Response

class AdminRepositoryImp(
    private val movieGoApi: MovieGoApi
) : AdminRepository {
    override suspend fun loginAdmin(admin: Login): Response<AuthResponse> {
        return movieGoApi.loginAdmin(admin)
    }

    override suspend fun signUpAdmin(admin: SignUp): Response<AuthResponse> {
        return movieGoApi.signUpAdmin(admin)
    }

    override suspend fun refreshAdminToken(): Response<AuthResponse> {
        return movieGoApi.refreshAdminToken()
    }
}