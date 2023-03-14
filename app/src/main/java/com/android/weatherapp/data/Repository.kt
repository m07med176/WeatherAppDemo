package com.android.weatherapp.data

import android.app.Application
import android.content.Context
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.android.weatherapp.data.local.HomeCash
import com.android.weatherapp.data.local.LocalDataSource
import com.android.weatherapp.data.local.RoomDB
import com.android.weatherapp.data.models.WeatherResponse
import com.android.weatherapp.data.remote.RemoteDataSource
import com.android.weatherapp.data.remote.RetrofiteInstance
import com.android.weatherapp.ui.alert.AlertModel
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
) {


    companion object {
        @Volatile
        private var INSTANCE:Repository?=null

        fun getInstance(app:Application):Repository{
            return INSTANCE?: synchronized(this){
                val apiCalls = RetrofiteInstance().apiCall()
                val remoteDataSource = RemoteDataSource(apiCalls)

                val roomDB = RoomDB.invoke(app)
                val homeCashDao = roomDB.homeCashDao()
                val alertDao = roomDB.alertDao()
                val localDataSource = LocalDataSource(homeCashDao,alertDao)
                Repository(remoteDataSource,localDataSource)
            }
        }

        // Create new overloaded function accept context
        fun getInstance(context:Context):Repository{
            return INSTANCE?: synchronized(this){
                val apiCalls = RetrofiteInstance().apiCall()
                val remoteDataSource = RemoteDataSource(apiCalls)
                val roomDB = RoomDB.invoke(context)
                val homeCashDao = roomDB.homeCashDao()
                val alertDao = roomDB.alertDao()
                val localDataSource = LocalDataSource(homeCashDao,alertDao)
                Repository(remoteDataSource,localDataSource)
            }
        }
    }

    fun getHomeCash(): Flow<HomeCash> {
        return localDataSource.getHomeCash()
    }

    suspend fun insertHomeCash(homeCash: HomeCash) {
        localDataSource.insertHomeCash(homeCash)
    }

    suspend fun deleteHomeCash(homeCash: HomeCash) {
        localDataSource.deleteHomeCash(homeCash)
    }

    fun getWeatherDetails(
        latitude: Double,
        longitude: Double,
        exclude: String? = null,
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


    // alert Dao
    fun getAlerts(): Flow<List<AlertModel>>{
        return localDataSource.getAlerts()
    }

    suspend fun insertAlert(alert: AlertModel):Long{
        return localDataSource.insertAlert(alert)
    }

    suspend fun deleteAlert(id: Int){
        localDataSource.deleteAlert(id)
    }

    suspend fun getAlert(id: Int): AlertModel{
        return localDataSource.getAlert(id)
    }
}