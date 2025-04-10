package com.example.moviego.util

import android.content.Context
import android.net.Uri
import okhttp3.ResponseBody
import org.json.JSONObject
import java.io.File

object Constants {
    const val USER_SETTINGS = "userSettings"
    const val USER_LOCATION = "User_Location"
    const val USER_TYPE = "user_type"
    const val USER_TOKEN = "USER_TOKEN"
    const val ADMIN = "Admin"
    const val USER = "User"
    const val BASE_URL = "http://10.0.2.2:3000/"

    const val SHOW_ID = "showId"
    const val THEATER_ID = "theaterId"
    const val MOVIE_ID = "movieID"
    const val BOOKING_ID = "bookingId"

    fun extractData(responseBody: ResponseBody?, element:String): String? {
        responseBody?.let {
            val responseJson = it.string()
            // Parse the JSON and extract token
            val jsonObject = JSONObject(responseJson)
            return jsonObject.optString(element, null)
        }
        return null
    }

    fun getFileFromUri(context: Context, uri: Uri): File {
        val contextResolver = context.contentResolver
        val inputStream = contextResolver.openInputStream(uri)
        val tempFile = File.createTempFile("${System.currentTimeMillis()}",".jpg",context.cacheDir)
        tempFile.outputStream().use { outputStream ->
            inputStream?.copyTo(outputStream)
        }

        return tempFile
    }
}