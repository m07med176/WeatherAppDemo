package com.android.weatherapp.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.weatherapp.data.ApiResponse
import com.android.weatherapp.data.Repository
import com.android.weatherapp.data.local.Favorite
import com.android.weatherapp.data.models.WeatherResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: Repository):ViewModel() {

    private val _weatherDetails = MutableStateFlow<ApiResponse<WeatherResponse>>(ApiResponse.OnLoading())
    val weatherDetails:StateFlow<ApiResponse<WeatherResponse>>
    get() = _weatherDetails



    fun getWeatherDetails(latitude:Double,
                          longitude:Double,
                          exclude:String?=null){

        viewModelScope.launch {
            repository.getWeatherDetails(latitude,longitude,exclude)
                .catch { _weatherDetails.value = ApiResponse.OnError(it.message.toString() ) }
                .collect{
                    repository.insertFavorite(Favorite(weather = it))
                    _weatherDetails.value = ApiResponse.OnSuccess(it)
                }

        }
    }
}