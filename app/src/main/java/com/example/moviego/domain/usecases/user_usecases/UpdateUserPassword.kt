package com.example.moviego.domain.usecases.user_usecases

import com.example.moviego.data.remote.Dao.UpdateBody
import com.example.moviego.util.Constants.extractData
import com.example.moviego.domain.repository.user.UserRepository


class UpdateUserPassword(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(data: UpdateBody): Result<String> {
        return try {
            val response = userRepository.updateUserPassword(data)
            if(response.isSuccessful) {
                val message = extractData(response.body(), "message")
                if(message != null) {
                    return Result.success(message)
                } else {
                    return Result.failure(Exception("Something went wrong"))
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