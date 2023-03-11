package com.android.weatherapp.data

import com.android.weatherapp.data.local.Favorite
import com.android.weatherapp.data.models.WeatherResponse
import kotlinx.coroutines.flow.Flow

interface RepositoryOperations {
    fun getFavorites(): Flow<List<Favorite>>

    suspend fun insertFavorite(favorite: Favorite)

    suspend fun deleteFavorite(favorite: Favorite)
    fun getWeatherDetails(
        latitude: Double,
        longitude: Double,
        exclude: String? = null,
    ): Flow<WeatherResponse>
}