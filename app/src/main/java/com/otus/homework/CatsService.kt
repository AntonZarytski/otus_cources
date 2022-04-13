package com.otus.homework

import com.otus.homework.networkUtils.NetworkResult
import retrofit2.http.GET
import retrofit2.http.Url

interface CatsService {

    @GET("random?animal_type=cat")
    suspend fun getCatFact(): NetworkResult<Fact>

    @GET
    suspend fun getCatImage(@Url url: String = "https://aws.random.cat/meow"): NetworkResult<Image>
}