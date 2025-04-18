package com.example.weatherapp.network

import com.example.weatherapp.data.CallState
import com.example.weatherapp.data.Interval
import com.example.weatherapp.data.LocationSearchResponse
import com.example.weatherapp.data.WeatherStackResponse
import com.example.weatherapp.util.fetch
import com.example.weatherapp.util.fetchData
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

@ViewModelScoped
class HistoricalWeatherRepo @Inject constructor(
    private val service: Service,
    private val ioDispatcher: CoroutineDispatcher
) {
    suspend fun getHistoricalData(
        query: String,
        date: List<String>,
        interval: Interval?
    ): CallState<WeatherStackResponse> {
        return fetch(
            ioDispatcher = ioDispatcher,
            api = {
                service.getHistoricalData(
                    query,
                    date.joinToString(";"),
                    if (interval != null) 1 else null,
                    interval?.value
                )
            }
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