package com.android.weatherapp.data

import androidx.lifecycle.MutableLiveData
import com.android.weatherapp.data.local.Favorite
import com.android.weatherapp.data.models.WeatherResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


/**
 * ViewModel Observers
 * - LiveData ( lifecycle aware )
 * - StateFLow , SharedFlow
 */
class FakeRepository(
    private var favoriteList: MutableList<Favorite> = mutableListOf<Favorite>(),
    private var weatherResponse: WeatherResponse = WeatherResponse()

) : RepositoryOperations{



    override fun getFavorites(): Flow<List<Favorite>>  = flow {
        emit(favoriteList)
    }

    override suspend fun insertFavorite(favorite: Favorite) {
        favoriteList.add(favorite)
    }

    override suspend fun deleteFavorite(favorite: Favorite) {
       favoriteList.remove(favorite)
    }

    override fun getWeatherDetails(
        latitude: Double,
        longitude: Double,
        exclude: String?,
    ): Flow<WeatherResponse>  = flow {
        emit(weatherResponse)
    }

}