package com.example.moviego.util

import okhttp3.ResponseBody
import org.json.JSONObject

object Constants {
    const val USER_SETTINGS = "userSettings"
    const val USER_LOCATION = "User_Location"
    const val USER_TYPE = "user_type"
    const val USER_TOKEN = "USER_TOKEN"
    const val ADMIN = "Admin"
    const val USER = "User"
    const val BASE_URL = "http://10.0.2.2:3000/"

    fun extractData(responseBody: ResponseBody?, element:String): String? {
        responseBody?.let {
            val responseJson = it.string()
            // Parse the JSON and extract token
            val jsonObject = JSONObject(responseJson)
            return jsonObject.optString(element, null)
        }
        return null
    }
}