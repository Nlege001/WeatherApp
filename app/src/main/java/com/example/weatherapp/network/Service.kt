package com.example.weatherapp.network

import com.example.weatherapp.data.LocationSearchResponse
import com.example.weatherapp.data.WeatherResponse
import com.example.weatherapp.data.WeatherStackResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface Service {

    @GET("current")
    suspend fun getCurrentWeather(
        @Query("query") query: String
    ): Response<WeatherResponse>

    @GET("autocomplete")
    suspend fun getAutoComplete(
        @Query("query") query: String
    ): Response<LocationSearchResponse>

    @GET("historical")
    suspend fun getHistoricalData(
        @Query("query") query: String,
        @Query("historical_date") date: String,
        @Query("hourly") hourly: Int?,
        @Query("interval") interval	: Int?,
    ) : Response<WeatherStackResponse>
}