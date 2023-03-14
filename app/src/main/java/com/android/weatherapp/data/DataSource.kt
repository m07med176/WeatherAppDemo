package com.android.weatherapp.data



import com.android.weatherapp.Constants
import com.android.weatherapp.data.local.HomeCash
import com.android.weatherapp.data.models.WeatherResponse
import com.android.weatherapp.ui.alert.AlertModel
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface DataSource {

    // Favorite Dao
    fun getHomeCash(): Flow<HomeCash>

    suspend fun insertHomeCash(homeCash: HomeCash)

    suspend fun deleteHomeCash(homeCash: HomeCash)

    // Network
    suspend fun getWeatherDetails(
        latitude: Double,
        longitude: Double,
        language: String = "en",
        apiKey: String = Constants.API_KEY,
        exclude: String? = null,
    ): Response<WeatherResponse>

    // Alert DAO
    fun getAlerts(): Flow<List<AlertModel>>

    suspend fun insertAlert(alert: AlertModel):Long

    suspend fun deleteAlert(id: Int)

    suspend fun getAlert(id: Int): AlertModel
}