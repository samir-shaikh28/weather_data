package com.example.weather.data.responses

import com.google.gson.annotations.SerializedName

data class CurrentWeather(
    @SerializedName("location") var location: LocationResponse? = null,
    @SerializedName("current") var current: CurrentResponse? = null,
    @SerializedName("forecast") var forecastData: ForeCastResponse? = null
)