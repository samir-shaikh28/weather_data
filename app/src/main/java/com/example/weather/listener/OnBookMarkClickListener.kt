package com.example.weather.listener

import com.example.weather.data.responses.LocationResponse

interface OnBookMarkClickListener {
    fun addToFavourites(location: LocationResponse?)
    fun removeFromFavourite(latLon: String)
}