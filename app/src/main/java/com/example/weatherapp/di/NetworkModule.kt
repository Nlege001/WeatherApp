package com.example.weatherapp.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    private val baseUrl = "http://api.weatherstack.com/"

    @Provides
    fun provideOkhttp(): OkHttpClient = OkHttpClient.Builder().build()

    @Provides
    fun provideRetrofit(client: OkHttpClient): Retrofit = Retrofit
        .Builder()
        .client(client)
        .baseUrl(baseUrl)
        .build()
}