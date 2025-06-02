package com.example.moviego.domain.usecases.admin_usecases

import com.example.moviego.domain.model.Booking
import com.example.moviego.domain.model.BookingDetails
import com.example.moviego.domain.repository.admin.AdminRepository
import com.example.moviego.util.Constants.extractData

class GetBookingsOfShow (
    private val adminRepository: AdminRepository
) {
    suspend operator fun invoke(showId: String): Result<List<Booking>> {
        return try {
            val response = adminRepository.getBookingsOfShow(showId)
            if(response.isSuccessful) {
                response.body()?.let { bookingDetails: List<Booking>  ->
                    Result.success(bookingDetails)
                }?: Result.failure(Exception("Some thing went Wrong"))
            } else {
                val message = extractData(response.errorBody(), "message")
                return Result.failure(Exception(message))
            }
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }
}