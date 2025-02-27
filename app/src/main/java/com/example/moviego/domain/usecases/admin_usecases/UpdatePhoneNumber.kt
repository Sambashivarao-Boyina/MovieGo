package com.example.moviego.domain.usecases.admin_usecases

import com.example.moviego.data.remote.Dao.UpdateBody
import com.example.moviego.domain.model.Admin
import com.example.moviego.domain.repository.admin.AdminRepository
import com.example.moviego.util.Constants.extractData

class UpdatePhoneNumber (
    private val adminRepository: AdminRepository
) {
    suspend operator fun invoke(data: UpdateBody): Result<Admin> {
        return try {
            val response = adminRepository.updatePhoneNumber(data)
            if(response.isSuccessful) {
                response.body().let { admin: Admin? ->
                    if(admin != null) {
                        return Result.success(admin)
                    } else {
                        return Result.failure(Exception("Some thing went wrong"))
                    }
                }
            } else {
                val message = extractData(response.errorBody(),"message")
                return Result.failure(Exception(message))
            }
        }catch (e: Exception) {
            return Result.failure(e)
        }
    }
}