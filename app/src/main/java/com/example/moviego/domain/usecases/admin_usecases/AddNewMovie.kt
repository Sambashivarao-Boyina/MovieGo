package com.example.moviego.domain.usecases.admin_usecases

import com.example.moviego.data.remote.Dao.NewMovie
import com.example.moviego.domain.repository.admin.AdminRepository
import com.example.moviego.util.Constants.extractData
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class AddNewMovie (
    private val adminRepository: AdminRepository
) {
    suspend operator fun invoke(poster: File, movie: NewMovie): Result<String> {
        return try {
            val requestFile = poster.asRequestBody("image/*".toMediaTypeOrNull())
            val body = MultipartBody.Part.createFormData("poster",poster.name, requestFile )

            val gson = Gson()
            val movieJson = gson.toJson(movie)

            // Convert JSON to RequestBody
            val movieRequestBody = movieJson.toRequestBody("application/json".toMediaTypeOrNull())

            val response = adminRepository.addNewMovie(movie = movieRequestBody, poster = body)

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