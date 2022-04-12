package com.otus.homework

import retrofit2.http.GET

interface CatsService {
    @GET("random?animal_type=cat")
    suspend fun getCatFact(): Result<Fact>
}