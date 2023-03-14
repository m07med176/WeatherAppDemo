package com.android.weatherapp.data

import com.android.weatherapp.data.local.HomeCash
import com.android.weatherapp.data.models.WeatherResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response

class FakeDataSource(
    private var homeCashLists: MutableList<HomeCash> = mutableListOf<HomeCash>(),
    private var weatherResponse: WeatherResponse = WeatherResponse(),
) : DataSource {


    override fun getHomeCash(): Flow<List<HomeCash>> = flow {
        emit(homeCashLists)
    }

    override suspend fun insertHomeCash(homeCash: HomeCash) {
        homeCashLists.add(homeCash)
    }

    override suspend fun deleteHomeCash(homeCash: HomeCash) {
        homeCashLists.remove(homeCash)
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