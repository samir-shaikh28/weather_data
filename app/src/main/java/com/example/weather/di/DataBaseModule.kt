package com.example.weather.di

import android.content.Context
import androidx.room.Room
import com.example.weather.data.db.WeatherDao
import com.example.weather.data.db.WeatherDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataBaseModule {

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): WeatherDatabase {
        return Room.databaseBuilder(
            context,
            WeatherDatabase::class.java,
            "weather_db.db"
        ).build()
    }

    @Singleton
    @Provides
    fun provideWeatherDao(database: WeatherDatabase): WeatherDao = database.weatherDao()
}