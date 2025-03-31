package com.example.moviego.domain.usecases.user_usecases

import com.example.moviego.domain.model.Movie
import com.example.moviego.domain.repository.user.UserRepository
import com.example.moviego.util.Constants.extractData

class GetUserMovieDetails (
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(movieId: String): Result<Movie> {
        return try {
            val response = userRepository.getMovieDetails(movieId)
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