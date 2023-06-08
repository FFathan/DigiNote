package com.example.capstoneprojectm3

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DatastorePreferences private constructor(private val dataStore: DataStore<Preferences>) {
    private val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
    private val API_AUTH_TOKEN = stringPreferencesKey("api_auth_token")

    fun getLoginStatus(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[IS_LOGGED_IN] ?: false
        }
    }
    suspend fun setLoginStatus(isLoggedIn: Boolean) {
        dataStore.edit { preferences ->
            preferences[IS_LOGGED_IN] = isLoggedIn
        }
    }

    fun getAuthToken(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[API_AUTH_TOKEN] ?: ""
        }
    }
    suspend fun setAuthToken(apiAuthToken: String) {
        dataStore.edit { preferences ->
            preferences[API_AUTH_TOKEN] = apiAuthToken
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: DatastorePreferences? = null

        fun getInstance(dataStore: DataStore<Preferences>): DatastorePreferences {
            return INSTANCE ?: synchronized(this) {
                val instance = DatastorePreferences(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}
