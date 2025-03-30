package com.example.moviego.domain.usecases.user_usecases

import com.example.moviego.domain.model.User
import com.example.moviego.domain.repository.user.UserRepository
import com.example.moviego.util.Constants.extractData

class GetUserDetails(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(): Result<User> {
        return try {
            val response = userRepository.getUserDetails()
            if(response.isSuccessful) {
                response.body().let { user: User? ->
                    if(user != null) {
                        return Result.success(user)
                    } else {
                        return Result.failure(Exception("Some thing went wrong"))
                    }
                }
            } else {
                val message = extractData(response.errorBody(), "message")
                return Result.failure(Exception(message))
            }
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }
}