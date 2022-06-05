package com.example.weather.data.responses

import com.google.gson.annotations.SerializedName

data class LocationResponse(
    @SerializedName("name") var name: String? = null,
    @SerializedName("region") var region: String? = null,
    @SerializedName("country") var country: String? = null,
    @SerializedName("tz_id") var timeZone: String? = null,
    @SerializedName("lat") var lat: Float = 0.0f,
    @SerializedName("lon") var lon: Float = 0.0f,
)