package com.android.weatherapp.data

import com.android.weatherapp.data.local.Favorite
import com.android.weatherapp.data.models.WeatherResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response

class FakeDataSource(
    private var favoriteList: MutableList<Favorite> = mutableListOf<Favorite>(),
    private var weatherResponse: WeatherResponse = WeatherResponse(),
) : DataSource {


    override fun getFavorites(): Flow<List<Favorite>> = flow {
        emit(favoriteList)
    }

    override suspend fun insertFavorite(favorite: Favorite) {
        favoriteList.add(favorite)
    }

    override suspend fun deleteFavorite(favorite: Favorite) {
        favoriteList.remove(favorite)
    }

    override suspend fun getWeatherDetails(
        latitude: Double,
        longitude: Double,
        language: String,
        apiKey: String,
        exclude: String?,
    ): Response<WeatherResponse> {
        return Response.success(weatherResponse)
    }
}