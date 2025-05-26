package com.example.moviego.domain.usecases.admin_usecases

import com.example.moviego.domain.model.ShowDetails
import com.example.moviego.domain.repository.admin.AdminRepository
import com.example.moviego.util.Constants.extractData

class OpenShow (
    private val adminRepository: AdminRepository
) {
    suspend operator fun invoke(showId: String): Result<ShowDetails> {
        return try{
            val response = adminRepository.openShow(showId)
            if(response.isSuccessful) {
                response.body()?.let { showDetails: ShowDetails ->
                    return Result.success(showDetails)
                } ?: Result.failure(Exception("something went wrong"))
            } else {
                val message = extractData(response.errorBody(), "message")
                return Result.failure(Exception(message))
            }
        }catch (e: Exception) {
            return Result.failure(e)
        }
    }
}