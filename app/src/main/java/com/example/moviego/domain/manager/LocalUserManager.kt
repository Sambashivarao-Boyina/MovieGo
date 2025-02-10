package com.example.moviego.domain.manager

import kotlinx.coroutines.flow.Flow

interface LocalUserManager {
    suspend fun saveUserLocation(location: String)
    fun getUserLocation():Flow<String>

    suspend fun saveUserType(type: String)
    fun getUserType(): Flow<String>

    suspend fun saveToken(token: String)
    fun getUserToken(): Flow<String>

    suspend fun logoutUser()
}