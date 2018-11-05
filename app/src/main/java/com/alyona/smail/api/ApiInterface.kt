package com.alyona.smail.api

import retrofit2.Call
import retrofit2.http.GET

interface ApiInterface {
    @GET("me/threads")
    fun getThreads(): Call<String>

}