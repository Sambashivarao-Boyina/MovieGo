package com.example.moviego.domain.usecases.user_usecases

import com.example.moviego.domain.model.Show
import com.example.moviego.domain.repository.user.UserRepository
import com.example.moviego.util.Constants.extractData

class GetMovieShows (
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(movieId: String): Result<List<Show>> {
        return try {
            val response = userRepository.getMovieShows(movieId)
            if(response.isSuccessful) {
                response.body()?.let { shows: List<Show> ->
                    return Result.success(shows)
                } ?: Result.failure(Exception("something went wrong"))
            } else {
                return Result.failure(Exception(extractData(response.errorBody(), "message")))
            }
        } catch (e: Exception) {
            return  Result.failure(e)
        }
    }
}