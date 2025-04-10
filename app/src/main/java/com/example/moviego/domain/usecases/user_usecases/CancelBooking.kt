package com.example.moviego.domain.usecases.user_usecases

import com.example.moviego.domain.repository.user.UserRepository
import com.example.moviego.util.Constants.extractData

class CancelBooking (
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(bookingId: String): Result<String> {
        return try {
            val response = userRepository.cancelBooking(bookingId)
            if(response.isSuccessful) {
                val message = extractData(response.body(), "message")
                return Result.success("Canceled Successfully")
            } else {
                val message = extractData(response.errorBody(), "message")
                return Result.failure(Exception(message))
            }
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }
}