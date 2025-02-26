package com.example.moviego.domain.usecases.admin_usecases

import com.example.moviego.data.remote.Dao.NewShow
import com.example.moviego.domain.repository.admin.AdminRepository
import com.example.moviego.util.Constants.extractData

class CreateNewShow (
    private val adminRepository: AdminRepository
) {
    suspend operator fun invoke(newShow: NewShow): Result<String> {
        return try {
            val response = adminRepository.createNewShow(newShow)
            if(response.isSuccessful) {
                val message = extractData(response.body(),"message")
                if(message != null) {
                    return Result.success(message)
                } else {
                    return Result.failure(Exception("Something went wrong"))
                }
            } else {
                val errorMessage = extractData(response.errorBody(),"message")
                return Result.failure(Exception(errorMessage))
            }
        }catch (e: Exception) {
            return Result.failure(e)
        }
    }
}