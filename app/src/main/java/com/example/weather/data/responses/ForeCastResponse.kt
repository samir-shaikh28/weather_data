package com.example.weather.data.responses

import com.google.gson.annotations.SerializedName


data class ForeCastResponse(
    @SerializedName("forecastday") var forecastday: List<ForeCastDay>? = null
)

data class ForeCastDay(
    @SerializedName("date") var date: String? = null,
    @SerializedName("day") var day: DayResponse? = null,
    @SerializedName("astro") var astro: AstroResponse? = null,
    @SerializedName("hour") var hour: List<HourResponse>? = null,
)

data class DayResponse(
    @SerializedName("maxtemp_c") var maxTempCel: Float = 0.0f,
    @SerializedName("maxtemp_f") var maxTempFer: Float = 0.0f,
    @SerializedName("mintemp_c") var minTempCel: Float = 0.0f,
    @SerializedName("mintemp_f") var minTempFer: Float = 0.0f,
    @SerializedName("maxwind_mph") var maxWindMph: Float = 0.0f,
    @SerializedName("maxwind_kph") var maxWindKph: Float = 0.0f,
    @SerializedName("daily_will_it_rain") var willItRain: Int = 0,
    @SerializedName("daily_chance_of_rain") var chanceOfRain: Int = 0,
    @SerializedName("daily_will_it_snow") var willItSnow: Int = 0,
    @SerializedName("daily_chance_of_snow") var chanceOfSnow: Int = 0,
    @SerializedName("condition") var dayCondition: WeatherCondition? = null,
)

data class AstroResponse(
    @SerializedName("sunrise") var sunrise: String? = null,
    @SerializedName("sunset") var sunset: String? = null,
)

data class HourResponse(
    @SerializedName("time") var time: String? = null,
    @SerializedName("temp_c") var tempCelcius: Float = 0.0f,
    @SerializedName("temp_f") var tempFerhanite: Float = 0.0f,
    @SerializedName("is_day") var isDat: Int = 0,
    @SerializedName("condition") var condition: WeatherCondition? = null
)