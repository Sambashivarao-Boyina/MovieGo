package com.example.moviego.domain.usecases.admin_usecases

import com.example.moviego.domain.model.Movie
import com.example.moviego.domain.repository.admin.AdminRepository
import com.example.moviego.util.Constants.extractData

class GetAllAdminMovies (
    private val adminRepository: AdminRepository
) {
    suspend operator fun  invoke(): Result<List<Movie>> {
        return try {
            val reponse = adminRepository.getAllAdminMovies()
            if(reponse.isSuccessful) {
                reponse.body()?.let { movies ->
                    Result.success(movies)
                } ?: Result.failure(Exception("No data available"))
            } else {
                val message = extractData(reponse.errorBody(), "message")
                return Result.failure(Exception(message))
            }
        }catch (e: Exception) {
            return Result.failure(e)
        }
    }
}