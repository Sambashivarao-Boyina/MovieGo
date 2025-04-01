package com.example.moviego.domain.usecases.user_usecases

import com.example.moviego.domain.model.ShowDetails
import com.example.moviego.domain.repository.user.UserRepository
import com.example.moviego.util.Constants.extractData

class GetShowDetailsForBooking (
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(showId: String): Result<ShowDetails> {
        return try {
            val response = userRepository.getShowDetailsForBooking(showId)
            if(response.isSuccessful) {
                response.body()?.let { showDetails: ShowDetails ->
                    return Result.success(showDetails)
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