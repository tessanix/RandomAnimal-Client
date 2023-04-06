package com.tmcoding.randomanimal_client.data

import okhttp3.*
import retrofit2.http.*

interface AnimalsApi {
    companion object {
        const val BASE_URL = "http://192.168.0.11:8850"
    }

    @GET("/randomanimal")
    suspend fun getRandomAnimal(): Animal

    @Multipart
    @POST("/newAnimal")
    suspend fun sendNewAnimal(
        @Part("name") name: RequestBody,
        @Part("description") description: RequestBody,
        @Part image: MultipartBody.Part
    )

}