package com.example.moviego.domain.usecases.user_usecases

import com.example.moviego.domain.model.BookingDetails
import com.example.moviego.domain.repository.user.UserRepository
import com.example.moviego.util.Constants.extractData

class GetBookingsList (
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(): Result<List<BookingDetails>> {
        return try {
            val response = userRepository.getBookingsList()
            if(response.isSuccessful) {
                return (response.body()?.let { bookingDetails: List<BookingDetails> ->
                    return Result.success(bookingDetails)
                } ?: Result.failure(Exception("Something went wrong")))
            } else {
                val message = extractData(response.errorBody(),"message")
                return Result.failure(Exception(message))
            }
        } catch (error: Exception) {
            return Result.failure(error)
        }
    }
}