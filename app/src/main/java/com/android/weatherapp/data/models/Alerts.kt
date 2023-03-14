package com.android.weatherapp.data.models

import com.google.gson.annotations.SerializedName

data class Alerts(
    @SerializedName("sender_name") var senderName: String? = null,
    @SerializedName("event") var event: String? = null,
    @SerializedName("start") var start: Long? = null,
    @SerializedName("end") var end: Long? = null,
    @SerializedName("description") var description: String? = null,
    @SerializedName("tags") var tags: List<String>
)