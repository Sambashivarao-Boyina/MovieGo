package com.example.moviego.domain.usecases.admin_usecases

import com.example.moviego.data.remote.Dao.NewMovie
import com.example.moviego.domain.repository.admin.AdminRepository
import com.example.moviego.util.Constants.extractData
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class AddNewMovie (
    private val adminRepository: AdminRepository
) {
    suspend operator fun invoke(poster: File, movie: NewMovie): Result<String> {
        return try {
            val requestFile = poster.asRequestBody("image/*".toMediaTypeOrNull())
            val body = MultipartBody.Part.createFormData("file",poster.name, requestFile )
            val response = adminRepository.addNewMovie(movie = movie, poster = body)

            if(response.isSuccessful) {
                return Result.success("Movie is Added")
            } else {
                val message = extractData(response.errorBody(), "message")
                return Result.failure(Exception(message))
            }
        } catch (e:Exception) {
            return Result.failure(e)
        }
    }
}