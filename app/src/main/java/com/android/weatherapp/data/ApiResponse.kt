package com.android.weatherapp.data

import com.android.weatherapp.data.models.WeatherResponse

sealed class ApiResponse<T>{
    class OnSuccess<T>(var data: T):ApiResponse<T>()
    class OnError<T>(var message:String):ApiResponse<T>()
    class OnLoading<T>():ApiResponse<T>()
}
