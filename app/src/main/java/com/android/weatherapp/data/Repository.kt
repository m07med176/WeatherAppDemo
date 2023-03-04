package com.android.weatherapp.data

import android.content.Context
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.android.weatherapp.Constants
import com.android.weatherapp.data.local.Favorite
import com.android.weatherapp.data.local.RoomDB
import com.android.weatherapp.data.models.WeatherResponse
import com.android.weatherapp.data.remote.ApiCalls
import com.android.weatherapp.data.remote.RetrofiteInstance
import retrofit2.Response
import retrofit2.http.GET

class Repository(context: Context) {
    private lateinit var network:ApiCalls
    private lateinit var room:RoomDB


    init {
        network =   RetrofiteInstance().apiCall()
        room = RoomDB.invoke(context)
    }

    suspend fun getFavorites():List<Favorite>{
        return room.favoriteDao().getFavorites()
    }

    suspend fun insertFavorite(favorite: Favorite){
        room.favoriteDao().insertFavorite(favorite)
    }

    suspend fun deleteFavorite(favorite: Favorite){
        room.favoriteDao().deleteFavorite(favorite)
    }

    suspend fun getWeatherDetails(
        latitude:Double,
        longitude:Double,
        exclude:String?=null
    ): WeatherResponse{
        val response = network.getWeatherDetails(
            latitude = latitude,
            longitude = longitude,
            exclude = exclude
        )

        if (response.isSuccessful){
            return response.body()?:WeatherResponse()
        }
       return  WeatherResponse()
    }
}