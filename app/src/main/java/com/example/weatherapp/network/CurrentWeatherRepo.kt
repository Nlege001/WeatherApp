package com.example.weatherapp.network

import com.example.weatherapp.data.CallState
import com.example.weatherapp.data.LocationSearchResponse
import com.example.weatherapp.data.WeatherResponse
import com.example.weatherapp.util.fetch
import com.example.weatherapp.util.fetchData
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

@ViewModelScoped
class CurrentWeatherRepo @Inject constructor(
    private val service: Service,
    private val ioDispatcher: CoroutineDispatcher
) {
    suspend fun getCurrentWeather(
        state: String,
    ): CallState<WeatherResponse> {
        return fetch(
            ioDispatcher = ioDispatcher,
            api = { service.getCurrentWeather(state) }
        )
    }

    suspend fun getAutoComplete(
        query: String
    ): LocationSearchResponse? {
        return fetchData(
            ioDispatcher = ioDispatcher,
            api = { service.getAutoComplete(query) }
        )
    }
}