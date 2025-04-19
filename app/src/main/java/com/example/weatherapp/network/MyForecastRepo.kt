package com.example.weatherapp.network

import com.example.weatherapp.data.CallState
import com.example.weatherapp.data.MarineForecastResponse
import com.example.weatherapp.util.fetch
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

@ViewModelScoped
class MyForecastRepo @Inject constructor(
    private val service: Service,
    private val ioDispatcher: CoroutineDispatcher
) {
    suspend fun getLocationForeCast(
        lat: String,
        long: String,
    ): CallState<MarineForecastResponse> {
        return fetch(
            ioDispatcher = ioDispatcher,
            api = {
                service.getMarineWeather(
                    latitude = lat,
                    longitude = long,
                    hourly = "1"
                )
            }
        )
    }
}