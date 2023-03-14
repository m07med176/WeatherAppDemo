package com.android.weatherapp.data.models

import com.google.gson.annotations.SerializedName

data class WeatherResponse(
    val current: Current?=null,
    val daily: List<Daily> = emptyList(),
    val hourly: List<Hourly> = emptyList(),
    @SerializedName("alerts") var alerts: List<Alerts>?=null,
    val lat: Double?=null,
    val lon: Double?=null,
    val timezone: String?=null,
    val timezone_offset: Int?=null
)