package com.example.weatherapp.network

import com.example.weatherapp.data.WeatherResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface Service {

    @GET("current")
    suspend fun getCurrentWeather(
        @Query("query") query: String
    ): Response<WeatherResponse>
}