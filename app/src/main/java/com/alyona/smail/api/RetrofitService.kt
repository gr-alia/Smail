package com.alyona.smail.api

import com.alyona.smail.constants.BASE_URL
import com.alyona.smail.constants.CONNECT_TIMEOUT
import com.alyona.smail.constants.READ_TIMEOUT
import com.google.android.gms.common.api.Api
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitService {


    fun getApi(): ApiInterface = getInstance(debugClientBuilder())

    private fun debugClientBuilder(): OkHttpClient.Builder {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS);

    }

    private fun defaultClientBuilder(): OkHttpClient.Builder {
        return OkHttpClient.Builder()
            .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
    }

    private fun getInstance(debugClientBuilder: OkHttpClient.Builder): ApiInterface {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(debugClientBuilder.build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiInterface::class.java)
    }
}
