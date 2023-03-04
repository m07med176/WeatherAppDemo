package com.android.weatherapp.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.android.weatherapp.models.WeatherResponse

@Entity(tableName = "favorite_table")
data class Favorite(
    @PrimaryKey(autoGenerate = true)
    val id:Int?=null,

    val weather:WeatherResponse

)
