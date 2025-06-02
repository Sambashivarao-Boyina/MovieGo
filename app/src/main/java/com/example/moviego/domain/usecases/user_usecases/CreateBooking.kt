package com.example.moviego.domain.usecases.user_usecases

import com.example.moviego.data.remote.Dao.BookingData
import com.example.moviego.domain.repository.user.UserRepository
import com.example.moviego.util.Constants.extractData

class CreateBooking (
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(data: BookingData): Result<String> {
        return try {
            val response = userRepository.createBooking(data)
            if(response.isSuccessful) {
                val bookingId = extractData(response.body(), "bookingId")
                if(bookingId != null) {
                    return Result.success(bookingId)
                } else {
                    return Result.failure(Exception("Something went Wrong"))
                }
            } else {
                val message = extractData(response.errorBody(), "message")
                return Result.failure(Exception(message))
            }
        } catch (e: Exception) {
            return  Result.failure(e)
        }
    }
}