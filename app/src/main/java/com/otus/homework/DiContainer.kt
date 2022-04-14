package com.otus.homework

import com.otus.homework.networkUtils.CustomResponseAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class DiContainer {

    private val timeoutSec: Long = 5

    private val retrofit by lazy {
        Retrofit.Builder()
            .client(httpClient.build())
            .baseUrl("https://cat-fact.herokuapp.com/facts/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CustomResponseAdapterFactory())
            .build()
    }

    private val httpClient by lazy {
        OkHttpClient.Builder().apply {
            readTimeout(timeoutSec, TimeUnit.SECONDS)
            connectTimeout(timeoutSec, TimeUnit.SECONDS)
        }
    }

    val service: CatsService by lazy { retrofit.create(CatsService::class.java) }
}