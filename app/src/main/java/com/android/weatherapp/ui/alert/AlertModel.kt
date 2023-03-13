package com.android.weatherapp.ui.alert

import androidx.room.Entity
import androidx.room.PrimaryKey


// TODO 1#1- Create Entity of Alert
@Entity(tableName = "alert")
data class AlertModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    // Mandatory
    var startTime: Long?=null,
    var endTime: Long?=null,
    var startDate: Long?=null,
    var endDate: Long?=null,

    // if you like
    var latitude: Double?=null,
    var longitude: Double?=null,
)