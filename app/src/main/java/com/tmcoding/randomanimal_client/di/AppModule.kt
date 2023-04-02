package com.tmcoding.randomanimal_client.di

import com.tmcoding.randomanimal_client.data.AnimalsApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class) // allow Module to stay alive since the app is alive
object AppModule {

    @Provides
    @Singleton // only create 1 instance of module in the entire app
    fun provideAnimalApi(): AnimalsApi {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(AnimalsApi.BASE_URL)
            .build()
            .create(AnimalsApi::class.java)
    }
}