package com.example.weather.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.weather.data.db.entity.FavouriteEntity

@Database(entities = [FavouriteEntity::class], version = 1)
abstract class WeatherDatabase : RoomDatabase() {
    abstract fun weatherDao(): WeatherDao
}