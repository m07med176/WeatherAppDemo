package com.android.weatherapp.data.local

import com.android.weatherapp.data.DataSource
import com.android.weatherapp.data.models.WeatherResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

class LocalDataSource(private val dao: FavoriteDao): DataSource {
    override fun getFavorites(): Flow<List<Favorite>> {
       return dao.getFavorites()
    }

    override suspend fun insertFavorite(favorite: Favorite) {
        dao.insertFavorite(favorite)
    }

    override suspend fun deleteFavorite(favorite: Favorite) {
        dao.deleteFavorite(favorite)
    }

    override suspend fun getWeatherDetails(
        latitude: Double,
        longitude: Double,
        language: String,
        apiKey: String,
        exclude: String?,
    ): Response<WeatherResponse> {
        TODO("Not yet implemented")
    }
}