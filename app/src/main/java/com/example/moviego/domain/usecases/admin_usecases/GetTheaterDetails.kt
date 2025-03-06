package com.example.moviego.domain.usecases.admin_usecases

import com.example.moviego.domain.model.TheaterDetails
import com.example.moviego.domain.repository.admin.AdminRepository
import com.example.moviego.util.Constants.extractData
import retrofit2.Response

class GetTheaterDetails(
    private val adminRepository: AdminRepository
) {
    suspend operator fun invoke(theaterId: String): Result<TheaterDetails> {
        return try {
            val response = adminRepository.getTheaterDetails(theaterId)
            if (response.isSuccessful) {
                response.body()?.let { theaterDetails: TheaterDetails ->
                    Result.success(theaterDetails)
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