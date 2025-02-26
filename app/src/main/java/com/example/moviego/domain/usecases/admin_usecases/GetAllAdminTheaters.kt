package com.example.moviego.domain.usecases.admin_usecases

import com.example.moviego.domain.model.TheaterDetails
import com.example.moviego.domain.repository.admin.AdminRepository
import com.example.moviego.util.Constants.extractData

class GetAllAdminTheaters (
    private val adminRepository: AdminRepository
) {
    suspend operator fun invoke(): Result<List<TheaterDetails>> {
        return try {
            val response = adminRepository.getAllAdminTheaters()
            if(response.isSuccessful) {
                response.body()?.let { theaters ->
                    Result.success(theaters)
                } ?: Result.failure(Exception("No data available"))
            } else {
                val message = extractData(response.errorBody(), "message")
                return Result.failure(Exception(message))
            }
        }catch (e: Exception) {
            return  Result.failure(e)
        }
    }
}