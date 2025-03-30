package com.example.moviego.data.repository.user

import com.example.moviego.data.remote.Dao.AuthResponse
import com.example.moviego.data.remote.Dao.Login
import com.example.moviego.data.remote.Dao.SignUp
import com.example.moviego.data.remote.Dao.UpdateBody
import com.example.moviego.data.remote.MovieGoApi
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
}