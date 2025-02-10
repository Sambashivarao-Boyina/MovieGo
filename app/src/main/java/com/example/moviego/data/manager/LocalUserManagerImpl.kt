package com.example.moviego.data.manager

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.moviego.domain.manager.LocalUserManager
import com.example.moviego.util.Constants
import com.example.moviego.util.Constants.USER_SETTINGS
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LocalUserManagerImpl(
    private val context: Context
): LocalUserManager {
    override suspend fun saveUserLocation(location: String) {
        context.datastore.edit { settings ->
            settings[PreferenceKeys.USER_LOCATION] = location
        }
    }

    override fun getUserLocation():Flow<String> {
        return context.datastore.data.map { prefernceKeys ->
            prefernceKeys[PreferenceKeys.USER_LOCATION] ?: ""
        }
    }

    override suspend fun saveUserType(type: String) {
        context.datastore.edit { settings ->
            settings[PreferenceKeys.USER_TYPE] = type
        }
    }

    override fun getUserType(): Flow<String> {
        return context.datastore.data.map { prefernceKeys ->
            prefernceKeys[PreferenceKeys.USER_TYPE] ?: ""
        }
    }

    override suspend fun saveToken(token: String) {
        context.datastore.edit { settings ->
            settings[PreferenceKeys.USER_TOKEN] = token
        }
    }

    override fun getUserToken(): Flow<String> {
        return context.datastore.data.map { preferenceKeys ->
            preferenceKeys[PreferenceKeys.USER_TOKEN] ?: ""
        }
    }

    override suspend fun logoutUser() {
        context.datastore.edit { settings ->
            settings.remove(PreferenceKeys.USER_LOCATION)
            settings.remove(PreferenceKeys.USER_TOKEN)
            settings.remove(PreferenceKeys.USER_TYPE)
        }
    }
}

private val Context.datastore: DataStore<Preferences> by preferencesDataStore(name = USER_SETTINGS)

private object PreferenceKeys {
    val USER_LOCATION = stringPreferencesKey(Constants.USER_LOCATION)
    val USER_TYPE = stringPreferencesKey(Constants.USER_TYPE)
    val USER_TOKEN = stringPreferencesKey(Constants.USER_TOKEN)
}