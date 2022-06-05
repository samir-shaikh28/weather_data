package com.example.weather.data.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

object WeatherDataStore {

    suspend fun setLatitudeLongitude(dataStore: DataStore<Preferences>, value: String) {
        dataStore.edit { pref ->
            pref[WeatherPrefKey.latLon] = value
        }
    }

    fun getLatitudeLongitude(dataStore: DataStore<Preferences>): Flow<String> {
        return dataStore.data.catch { exception ->
            emit(handleException(exception))
        }.map { pref ->
            pref[WeatherPrefKey.latLon] ?: "0.0,0.0"
        }
    }

    private fun handleException(exception: Throwable): Preferences {
        if (exception is IOException) {
            return emptyPreferences()
        } else {
            throw exception
        }
    }

    object WeatherPrefKey {
        val latLon = stringPreferencesKey("latLon")
    }
}