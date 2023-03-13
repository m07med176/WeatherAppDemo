package com.android.weatherapp.ui.alert

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


/**
 * Do whatever you want from date time pattern
 * dd MMM, hh:mm aa
 * dd MMM
 * hh:mm aa
 * d MMM, yyyy
 */
@SuppressLint("SimpleDateFormat")
fun dateTimeConverterTimestampToString(dt: Long,context: Context): String? {
    val timeStamp = Date(TimeUnit.SECONDS.toMillis(dt))
    return SimpleDateFormat("dd MMM, hh:mm aa",getCurrentLocale(context)).format(timeStamp)
}

@SuppressLint("SimpleDateFormat")
fun dayConverterToString(dt: Long,context: Context): String? {
    val timeStamp = Date(TimeUnit.SECONDS.toMillis(dt))
    return SimpleDateFormat("dd MMM",getCurrentLocale(context)).format(timeStamp)
}

@SuppressLint("SimpleDateFormat")
fun timeConverterToString(dt: Long,context: Context): String? {
    val timeStamp = Date(TimeUnit.SECONDS.toMillis(dt))
    return SimpleDateFormat("hh:mm aa",getCurrentLocale(context)).format(timeStamp)
}


fun getCurrentLocale(context: Context): Locale? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        context.resources.configuration.locales[0]
    } else {
        context.resources.configuration.locale
    }
}

fun convertDateToLong(date: String,context: Context): Long {
    val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy", getCurrentLocale(context))
    val timestamp: Date = simpleDateFormat.parse(date) as Date
    return timestamp.time
}
