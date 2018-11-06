package com.alyona.smail.api

import com.alyona.smail.model.ThreadsListResponse
import kotlinx.coroutines.Deferred
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiInterface {
    @GET("{id}/threads")
    fun getThreads(@Path("id") id: String): Deferred<ThreadsListResponse>

}