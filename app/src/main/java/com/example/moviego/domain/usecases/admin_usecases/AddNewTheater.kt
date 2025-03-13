package com.example.moviego.domain.usecases.admin_usecases

import com.example.moviego.data.remote.Dao.NewTheater
import com.example.moviego.domain.repository.admin.AdminRepository
import com.example.moviego.util.Constants.extractData
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class AddNewTheater (
    private val adminRepository: AdminRepository
) {
    suspend operator fun invoke(image: File, newTheater: NewTheater): Result<String> {
        return try {
            val requestFile = image.asRequestBody("image/*".toMediaTypeOrNull())
            val body = MultipartBody.Part.createFormData("poster",image.name, requestFile )

            val gson = Gson()
            val theaterJson = gson.toJson(newTheater)

            // Convert JSON to RequestBody
            val theaterRequestBody = theaterJson.toRequestBody("application/json".toMediaTypeOrNull())

            val response = adminRepository.addNewTheater(newTheater = theaterRequestBody, image = body)

            if(response.isSuccessful) {
                return Result.success("Theater is Added")
            } else {
                val message = extractData(response.errorBody(), "message")
                return Result.failure(Exception(message))
            }
        } catch (e:Exception) {
            return Result.failure(e)
        }
    }
}