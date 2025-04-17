package com.example.weatherapp.di

import com.example.weatherapp.network.Service
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
    fun provideAuthInterceptor(): AuthInterceptor = AuthInterceptor()

    @Provides
    fun provideOkhttp(interceptor: AuthInterceptor): OkHttpClient = OkHttpClient
        .Builder()
        .addInterceptor(interceptor)
        .build()

    @Provides
    fun provideRetrofit(client: OkHttpClient): Retrofit = Retrofit
        .Builder()
        .client(client)
        .baseUrl(baseUrl)
        .build()

    @Provides
    fun provideService(retrofit: Retrofit): Service = retrofit.create(Service::class.java)
}