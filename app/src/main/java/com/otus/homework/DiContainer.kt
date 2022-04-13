package com.otus.homework

import com.otus.homework.networkUtils.CustomResponseAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DiContainer {

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://cat-fact.herokuapp.com/facts/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CustomResponseAdapterFactory())
            .build()
    }

    val service by lazy { retrofit.create(CatsService::class.java) }
}