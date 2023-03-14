package com.android.weatherapp.data.remote

import com.android.weatherapp.data.DataSource
import com.android.weatherapp.data.local.HomeCash
import com.android.weatherapp.data.models.WeatherResponse
import com.android.weatherapp.ui.alert.AlertModel
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

class RemoteDataSource(private val  api: ApiCalls) : DataSource{
    override fun getHomeCash(): Flow<HomeCash> {
        TODO("Not yet implemented")
    }

    override suspend fun insertHomeCash(homeCash: HomeCash) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteHomeCash(homeCash: HomeCash) {
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

    override fun getAlerts(): Flow<List<AlertModel>> {
        TODO("Not yet implemented")
    }

    override suspend fun insertAlert(alert: AlertModel): Long {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAlert(id: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun getAlert(id: Int): AlertModel {
        TODO("Not yet implemented")
    }
}