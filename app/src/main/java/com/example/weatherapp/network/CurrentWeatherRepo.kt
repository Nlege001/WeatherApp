package com.example.weatherapp.network

import com.example.weatherapp.data.CallState
import com.example.weatherapp.data.WeatherResponse
import com.example.weatherapp.util.fetch
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class CurrentWeatherRepo @Inject constructor(
    private val service: Service
) {
    suspend fun getCurrentWeather(
        state: String
    ): CallState<WeatherResponse> {
        return fetch { service.getCurrentWeather(state) }
    }
}