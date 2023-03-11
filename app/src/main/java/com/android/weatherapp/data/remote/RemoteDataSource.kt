package com.android.weatherapp.data.remote

import com.android.weatherapp.data.DataSource
import com.android.weatherapp.data.local.Favorite
import com.android.weatherapp.data.models.WeatherResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

class RemoteDataSource(private val  api: ApiCalls) : DataSource{
    override fun getFavorites(): Flow<List<Favorite>> {
        TODO("Not yet implemented")
    }

    override suspend fun insertFavorite(favorite: Favorite) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteFavorite(favorite: Favorite) {
        TODO("Not yet implemented")
    }

    override suspend fun getWeatherDetails(
        latitude: Double,
        longitude: Double,
        language: String,
        apiKey: String,
        exclude: String?,
    ): Response<WeatherResponse> {
        return api.getWeatherDetails(latitude,longitude,language,apiKey,exclude)
    }
}