package com.example.moviego.domain.usecases.user_usecases

import com.example.moviego.domain.model.Booking
import com.example.moviego.domain.repository.user.UserRepository
import com.example.moviego.util.Constants.extractData

class GetBookingDetails (
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(bookingId: String):Result<Booking> {
        return try {
            val response = userRepository.getBookingDetails(bookingId)
            if(response.isSuccessful) {
                response.body()?.let { booking: Booking ->
                    return Result.success(booking)
                } ?: Result.failure(Exception("Something went wrong"))
            } else {
                val message = extractData(response.errorBody(),"message")
                return Result.failure(Exception(message))
            }
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }
}