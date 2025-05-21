package com.example.moviego.domain.usecases.user_usecases

import com.example.moviego.domain.model.Movie
import com.example.moviego.domain.repository.user.UserRepository
import com.example.moviego.util.Constants.extractData

class GetMovies (
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(state: String, city: String): Result<List<Movie>> {
        return try {
            val response = userRepository.getMovies(state = state, city = city)
            if(response.isSuccessful) {
                response.body()?.let { movies: List<Movie> ->
                    return Result.success(movies)
                } ?: Result.failure(Exception("Something went worng"))
            } else {
                val message = extractData(response.errorBody(), "message")
                return Result.failure(Exception(message))
            }
        }catch (e: Exception) {
            return Result.failure(e)
        }
    }
}