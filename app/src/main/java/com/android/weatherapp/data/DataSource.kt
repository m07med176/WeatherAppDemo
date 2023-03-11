package com.android.weatherapp.data


import com.android.weatherapp.Constants
import com.android.weatherapp.data.local.Favorite
import com.android.weatherapp.data.models.WeatherResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface DataSource {

    // Favorite Dao
    fun getFavorites(): Flow<List<Favorite>>

    suspend fun insertFavorite(favorite: Favorite)

    suspend fun deleteFavorite(favorite: Favorite)

    // Network
    suspend fun getWeatherDetails(
        latitude: Double,
        longitude: Double,
        language: String = "en",
        apiKey: String = Constants.API_KEY,
        exclude: String? = null,
    ): Response<WeatherResponse>
}