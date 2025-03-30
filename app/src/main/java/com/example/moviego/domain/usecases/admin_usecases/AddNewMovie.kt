package com.example.moviego.domain.usecases.admin_usecases

import com.example.moviego.domain.repository.admin.AdminRepository
import com.example.moviego.util.Constants.extractData


class AddNewMovie (
    private val adminRepository: AdminRepository
) {
    suspend operator fun invoke(movieName: String): Result<String> {
        return try {
            val response = adminRepository.addNewMovie(movieName = movieName)

            if(response.isSuccessful) {
                return Result.success("Movie is Added")
            } else {
                val message = extractData(response.errorBody(), "message")
                return Result.failure(Exception(message))
            }
        } catch (e:Exception) {
            return Result.failure(e)
        }
    }
}