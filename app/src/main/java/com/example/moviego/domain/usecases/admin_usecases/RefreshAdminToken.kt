package com.example.moviego.domain.usecases.admin_usecases

import com.example.moviego.data.remote.Dao.AuthResponse
import com.example.moviego.domain.manager.LocalUserManager
import com.example.moviego.domain.repository.admin.AdminRepository
import com.example.moviego.util.Constants.ADMIN
import com.example.moviego.util.Constants.extractData

class RefreshAdminToken(
    private val adminRepository: AdminRepository,
    private val localUserManager: LocalUserManager
) {
    suspend operator fun invoke():Result<String> {
        return try {
            val response = adminRepository.refreshAdminToken()
            if(response.isSuccessful) {
                response.body()?.let { authResponse: AuthResponse ->
                    if(authResponse.token.isNotEmpty()){
                        localUserManager.saveToken(authResponse.token)
                        localUserManager.saveUserType(ADMIN)
                        return Result.success(authResponse.message)
                    }

                }
                return Result.failure(Exception("Some thing went wrong"))
            } else {
                val message = extractData(response.errorBody(),"message")
                localUserManager.logoutUser()
                return Result.failure(Exception(message))
            }
        }catch (e:Exception) {
            return  Result.failure(e)
        }
    }
}