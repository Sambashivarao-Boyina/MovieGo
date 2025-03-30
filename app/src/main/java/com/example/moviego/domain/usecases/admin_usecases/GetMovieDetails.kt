package com.example.moviego.domain.usecases.admin_usecases

import com.example.moviego.domain.model.Movie
import com.example.moviego.domain.repository.admin.AdminRepository
import com.example.moviego.util.Constants.extractData

class GetMovieDetails (
    private val adminRepository: AdminRepository
) {
    suspend operator fun invoke(movieId:String): Result<Movie> {
        return try {
            val response = adminRepository.getMovieDetails(movieId)
            if(response.isSuccessful) {
                response.body().let { movie: Movie? ->
                    if(movie != null) {
                        return Result.success(movie)
                    } else {
                        return Result.failure(Exception("Some thing went wrong"))
                    }
                }
            } else {
                val message = extractData(response.errorBody(), "message")
                return Result.failure(Exception(message))
            }
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }
}