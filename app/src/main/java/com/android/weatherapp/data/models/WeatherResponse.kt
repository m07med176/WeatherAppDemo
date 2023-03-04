package com.android.weatherapp.data.models

data class WeatherResponse(
    val current: Current?=null,
    val daily: List<Daily> = emptyList(),
    val hourly: List<Hourly> = emptyList(),
    val lat: Double?=null,
    val lon: Double?=null,
    val timezone: String?=null,
    val timezone_offset: Int?=null
)