package com.android.weatherapp.data.remote

import com.android.weatherapp.Constants
import okhttp3.OkHttp
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofiteInstance {

    val retrofit:Retrofit by lazy{
        // Interceptor Logger
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        val okHttp = OkHttpClient().newBuilder().addInterceptor(interceptor).build()

        // Retrofit Builder
        Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttp)
            .build()
    }

    fun apiCall(): ApiCalls {
        return retrofit.create(ApiCalls::class.java)
    }
}