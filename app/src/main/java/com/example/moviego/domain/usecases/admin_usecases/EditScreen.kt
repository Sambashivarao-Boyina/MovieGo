package com.example.moviego.domain.usecases.admin_usecases

import com.example.moviego.data.remote.Dao.NewScreen
import com.example.moviego.domain.model.TheaterDetails
import com.example.moviego.domain.repository.admin.AdminRepository
import com.example.moviego.util.Constants.extractData

class EditScreen (
    private val adminRepository: AdminRepository
) {
    suspend operator fun invoke(screenId: String, editScreen: NewScreen): Result<TheaterDetails> {
        return try {
            val response = adminRepository.editScreen(editScreen = editScreen, screenId = screenId)
            if (response.isSuccessful) {
                response.body()?.let { theaterDetails: TheaterDetails ->
                    Result.success(theaterDetails)
                } ?: Result.failure(Exception("Something went wrong"))
            } else {
                val message = extractData(response.errorBody(),"message")
                return Result.failure(Exception(message))
            }
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }
}