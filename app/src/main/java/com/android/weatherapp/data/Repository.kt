package com.android.weatherapp.data

import android.app.Application
import android.content.Context
import com.android.weatherapp.data.local.Favorite
import com.android.weatherapp.data.local.LocalDataSource
import com.android.weatherapp.data.local.RoomDB
import com.android.weatherapp.data.models.WeatherResponse
import com.android.weatherapp.data.remote.RemoteDataSource
import com.android.weatherapp.data.remote.RetrofiteInstance
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/*
6- Get Instance from Retrofit   DONE
5- Create Remote DataSource     DONE
4- Get Instance of Room         DONE
3- Create LocalDataSource       DONE
2- Context                      DONE
1- Create Repository
 */
class Repository(
    private val remoteDataSource: DataSource,
    private val localDataSource: DataSource
) : RepositoryOperations {


    companion object {
        @Volatile
        private var INSTANCE:Repository?=null

        fun getInstance(app:Application):Repository{
            return INSTANCE?: synchronized(this){
                val apiCalls = RetrofiteInstance().apiCall()
                val remoteDataSource = RemoteDataSource(apiCalls)

                val roomDB = RoomDB.invoke(app)
                val favoriteDao = roomDB.favoriteDao()
                val localDataSource = LocalDataSource(favoriteDao)
                Repository(remoteDataSource,localDataSource)
            }
        }
    }

    override fun getFavorites(): Flow<List<Favorite>> {
        return localDataSource.getFavorites()
    }

    override suspend fun insertFavorite(favorite: Favorite) {
        localDataSource.insertFavorite(favorite)
    }

    override suspend fun deleteFavorite(favorite: Favorite) {
        localDataSource.deleteFavorite(favorite)
    }

    override fun getWeatherDetails(
        latitude: Double,
        longitude: Double,
        exclude: String?,
    ) = flow {
        val response = remoteDataSource.getWeatherDetails(
            latitude = latitude,
            longitude = longitude,
            exclude = exclude
        )

        if (response.isSuccessful) {
            emit(response.body() ?: WeatherResponse())
        } else {
            emit(WeatherResponse())

        }

    }
}