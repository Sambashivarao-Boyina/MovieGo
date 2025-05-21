package com.example.moviego.domain.usecases.user_usecases

import com.example.moviego.domain.manager.LocalUserManager

class SetLocation (
    private val localUserManager: LocalUserManager
){
    suspend operator fun invoke(location: String): Result<String> {
        return try {
            localUserManager.saveUserLocation(location)
            return Result.success("Location Saved")
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }
}