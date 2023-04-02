package com.tmcoding.randomanimal_client.data

import retrofit2.http.GET

interface AnimalsApi {

    @GET("/randomanimal")
    suspend fun getRandomAnimal(): Animal

    companion object {
        const val BASE_URL = "http://192.168.0.11:8100"
    }

}