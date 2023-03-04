package com.android.weatherapp.remote

import com.android.weatherapp.Constants
import com.android.weatherapp.models.WeatherResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiCalls {

    /**
     * # OneCall
     * - lat : Required
     * - lon: Required
     * - appid :Required
     *  - exclude : Optional
     *  ### Ex: {{BASE_URL}}?lat=30.61554342119405&lon=32.27797547385768&appid={{API_KEY}}
     */
    @GET("onecall")
    suspend fun getWeatherDetails(
        @Query("lat") latitude:Double,
        @Query("lon") longitude:Double,
        @Query("appid") apiKey:String = Constants.API_KEY,
        @Query("exclude") exclude:String?=null
    ): Response<WeatherResponse>
}