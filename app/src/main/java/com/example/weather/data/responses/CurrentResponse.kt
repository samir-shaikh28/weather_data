package com.example.weather.data.responses

import com.google.gson.annotations.SerializedName


data class CurrentResponse(
    @SerializedName("last_updated") var lastUpdate: String? = null,
    @SerializedName("temp_c") var celcius: Float = 0.0f,
    @SerializedName("temp_f") var ferhanite: Float = 0.0f,
    @SerializedName("is_day") var isDay: Int = 0,
    @SerializedName("condition") var condition: WeatherCondition? = null,
    @SerializedName("wind_mph") var windMph: Float = 0.0f,
    @SerializedName("wind_kph") var windKph: Float = 0.0f,
    @SerializedName("wind_degree") var windDegree: Int = 0,
    @SerializedName("pressure_mb") var pressureMb: String? = null,
    @SerializedName("pressure_in") var pressureIn: Double = 0.0,
    @SerializedName("humidity") var humidity: Int = 0,
    @SerializedName("cloud") var cloud: Int = 0,
    @SerializedName("feelslike_c") var feelslikeC: Float = 0.0f,
    @SerializedName("feelslike_f") var feelslikeF: Float = 0.0f
)

data class WeatherCondition(
    @SerializedName("text") var text: String? = null,
    @SerializedName("icon") var icon: String? = null,
    @SerializedName("code") var code: Int = 0,
)