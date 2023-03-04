package com.android.weatherapp.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.weatherapp.data.Repository
import com.android.weatherapp.data.local.Favorite
import com.android.weatherapp.data.models.WeatherResponse
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: Repository):ViewModel() {

    private val _weatherDetails = MutableLiveData<WeatherResponse>()
    val weatherDetails:LiveData<WeatherResponse>
    get() = _weatherDetails

    fun getWeatherDetails(latitude:Double,
                          longitude:Double,
                          exclude:String?=null){

        viewModelScope.launch {
            val data  = repository.getWeatherDetails(latitude,longitude,exclude)
            repository.insertFavorite(Favorite(weather = data))
            _weatherDetails.value = data
        }
    }
}