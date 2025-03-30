package com.example.moviego.domain.usecases.user_usecases

import com.example.moviego.domain.manager.LocalUserManager

class LogoutUser(
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