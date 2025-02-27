package com.example.moviego.domain.usecases.admin_usecases

import com.example.moviego.data.remote.Dao.UpdateBody
import com.example.moviego.domain.repository.admin.AdminRepository
import com.example.moviego.util.Constants.extractData

class UpdatePassword (
    private val adminRepository: AdminRepository
) {
    suspend operator fun invoke(data: UpdateBody): Result<String> {
        return try {
            val response = adminRepository.updatePassword(data)
            if(response.isSuccessful) {
                val message = extractData(response.body(), "message")
                if(message != null) {
                    return Result.success(message)
                } else {
                    return Result.failure(Exception("Something went wrong"))
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