package com.example.moviego.domain.usecases.admin_usecases

import com.example.moviego.domain.model.ShowDetails
import com.example.moviego.domain.repository.admin.AdminRepository
import com.example.moviego.util.Constants.extractData

class GetShowDetails (
    private val adminRepository: AdminRepository
) {
    suspend operator fun invoke(showId:String): Result<ShowDetails> {
        return try {
            val response = adminRepository.getShowDetails(showId)
            if(response.isSuccessful) {
                return Result.success(response.body()!!)
            } else {
                val message = extractData(response.errorBody(), "message")
                return Result.failure(Exception(message))
            }
        }catch (e: Exception) {
            return Result.failure(e)
        }
    }
}