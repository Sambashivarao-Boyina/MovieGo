package com.example.moviego.domain.usecases.user_usecases

import com.example.moviego.domain.manager.LocalUserManager
import kotlinx.coroutines.flow.first

class GetLocation (
    private val localUserManager: LocalUserManager
) {
    suspend operator fun invoke(): Result<String> {
        return try {
            val place = localUserManager.getUserLocation().first()
            return Result.success(place)
        }catch (e: Exception) {
            return Result.failure(e)
        }
    }
}