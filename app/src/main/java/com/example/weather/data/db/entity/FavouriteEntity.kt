package com.example.weather.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "favourite_table")
data class FavouriteEntity(
    @PrimaryKey @ColumnInfo(name = "latLon") val latLon: String,
    @ColumnInfo(name = "name") val name: String?,
    @ColumnInfo(name = "region") val region: String?,
)