package com.example.moviego.domain.usecases.admin_usecases

import com.example.moviego.domain.model.Show
import com.example.moviego.domain.repository.admin.AdminRepository
import com.example.moviego.util.Constants.extractData

class GetAllShows (
    private val adminRepository: AdminRepository
) {
    suspend operator fun invoke():Result<List<Show>> {
        return try {
            val response = adminRepository.getAllShows()
            if(response.isSuccessful) {
                response.body()?.let { shows ->
                    Result.success(shows)
                } ?: Result.failure(Exception("No data available"))
            } else {
                val message = extractData(response.errorBody(), "message")
                Result.failure(Exception(message))
            }
        }catch (e:Exception) {
            return Result.failure(e)
        }
    }
}