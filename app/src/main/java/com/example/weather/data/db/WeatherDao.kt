package com.example.weather.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.weather.data.db.entity.FavouriteEntity

@Dao
interface WeatherDao {

    @Query("SELECT * FROM favourite_table")
    suspend fun getFavouriteCities(): List<FavouriteEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavouriteItem(favouriteItem: FavouriteEntity)

    @Query("DELETE FROM favourite_table WHERE latLon = :latLon")
    suspend fun deleteFavouriteItem(latLon: String)


    @Query("SELECT EXISTS(SELECT * FROM favourite_table WHERE  latLon = :latLon)")
    fun isRowIsExist(latLon: String) : Boolean
}