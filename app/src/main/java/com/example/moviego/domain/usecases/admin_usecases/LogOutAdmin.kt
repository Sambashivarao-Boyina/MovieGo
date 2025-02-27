package com.example.moviego.domain.usecases.admin_usecases

import com.example.moviego.domain.manager.LocalUserManager
import com.example.moviego.domain.repository.admin.AdminRepository

class LogOutAdmin (
    private val localUserManager: LocalUserManager
) {
    suspend operator fun invoke():Result<String> {
        return try {
            localUserManager.logoutUser()
            return Result.success("Admin Logged Out")
        }catch (e:Exception) {
            return Result.failure(e)
        }
    }
}