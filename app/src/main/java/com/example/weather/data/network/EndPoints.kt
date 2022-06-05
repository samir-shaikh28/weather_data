package com.example.weather.data.network

import com.example.weather.data.responses.CurrentWeather
import retrofit2.http.GET
import retrofit2.http.Query

interface EndPoints {

    companion object {
        const val API_KEY = "c352c37efabd40ebbdc101608220406"
    }

    @GET("/v1/forecast.json")
    suspend fun getWeatherData(
        @Query("key") apiKey: String = API_KEY, @Query("q") latLon: String, @Query("days")
        days: Int = 10
    ):
            CurrentWeather
}