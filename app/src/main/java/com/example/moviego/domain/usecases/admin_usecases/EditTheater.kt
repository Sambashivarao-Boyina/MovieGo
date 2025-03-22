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

class EditTheater (
    private val adminRepository: AdminRepository
) {
    suspend operator fun invoke(theaterId: String, image: File?, editTheater: NewTheater): Result<String> {
        return try {

            var body: MultipartBody.Part? = null
            image?.let {
                val requestFile = image.asRequestBody("image/*".toMediaTypeOrNull())
                body = MultipartBody.Part.createFormData("image",image.name, requestFile )
            }

            val gson = Gson()
            val theaterJson = gson.toJson(editTheater)

            // Convert JSON to RequestBody
            val theaterRequestBody = theaterJson.toRequestBody("application/json".toMediaTypeOrNull())

            val response = adminRepository.editTheater(editTheater = theaterRequestBody, image = body, theaterId = theaterId)

            if(response.isSuccessful) {
                return Result.success("Theater is Edited")
            } else {
                val message = extractData(response.errorBody(), "message")
                return Result.failure(Exception(message))
            }
        } catch (e:Exception) {
            return Result.failure(e)
        }
    }
}