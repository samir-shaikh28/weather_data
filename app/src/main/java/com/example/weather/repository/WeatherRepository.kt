package com.example.weather.repository

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.example.weather.data.datastore.WeatherDataStore
import com.example.weather.data.db.WeatherDao
import com.example.weather.data.db.entity.FavouriteEntity
import com.example.weather.data.network.EndPoints
import com.example.weather.data.responses.CurrentWeather
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.take
import javax.inject.Inject

class WeatherRepository @Inject constructor(private val endPoints: EndPoints, private val dao: WeatherDao) {

    suspend fun fetchWeatherData(latLon: String): CurrentWeather {
        return endPoints.getWeatherData(latLon = latLon)

    }

    suspend fun bookMarkLocation(latLon: String, name: String?, region: String?) {
        dao.insertFavouriteItem(FavouriteEntity(latLon, name, region))
    }

    suspend fun removeFromFavourite(latLon: String) {
        dao.deleteFavouriteItem(latLon)
    }

    suspend fun fetchFavCityList(): List<FavouriteEntity> {
        return dao.getFavouriteCities()
    }

    suspend fun isRowIsExist(latLon: String): Boolean {
        return dao.isRowIsExist(latLon)
    }

}