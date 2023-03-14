package com.android.weatherapp.data.local

import com.android.weatherapp.data.DataSource
import com.android.weatherapp.data.models.WeatherResponse
import com.android.weatherapp.ui.alert.AlertDao
import com.android.weatherapp.ui.alert.AlertModel
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

class LocalDataSource(private val daoHome: HomeDao,private val daoAlert: AlertDao): DataSource {
    override fun getHomeCash(): Flow<HomeCash> {
       return daoHome.getHomeCash()
    }

    override suspend fun insertHomeCash(homeCash: HomeCash) {
        daoHome.insertHomeCash(homeCash)
    }

    override suspend fun deleteHomeCash(homeCash: HomeCash) {
        daoHome.deleteHomeCash(homeCash)
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

    override fun getAlerts(): Flow<List<AlertModel>> {
        return daoAlert.getAlerts()
    }

    override suspend fun insertAlert(alert: AlertModel): Long {
        return daoAlert.insertAlert(alert)
    }

    override suspend fun deleteAlert(id: Int) {
        daoAlert.deleteAlert(id)
    }

    override suspend fun getAlert(id: Int): AlertModel {
        return daoAlert.getAlert(id)
    }
}