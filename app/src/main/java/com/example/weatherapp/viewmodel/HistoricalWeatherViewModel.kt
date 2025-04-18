package com.example.weatherapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.CallState
import com.example.weatherapp.data.Interval
import com.example.weatherapp.data.LocationSearchResponse
import com.example.weatherapp.data.WeatherStackResponse
import com.example.weatherapp.network.HistoricalWeatherRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoricalWeatherViewModel @Inject constructor(
    private val historicalWeatherRepo: HistoricalWeatherRepo
) : ViewModel() {
    private val _historicalData =
        MutableStateFlow<CallState<WeatherStackResponse>>(CallState.EmptyContent)
    val historicalData: StateFlow<CallState<WeatherStackResponse>> = _historicalData

    private val _autoCompleteResponse =
        MutableStateFlow<LocationSearchResponse?>(null)
    val autoCompleteResponse: StateFlow<LocationSearchResponse?> = _autoCompleteResponse

    fun getHistoricalData(
        userInput: String,
        date: List<String>,
        interval: Interval?

    ) {
        viewModelScope.launch {
            _historicalData.value = CallState.Loading
            _historicalData.value =
                historicalWeatherRepo.getHistoricalData(userInput, date, interval)
        }
    }

    fun getAutoCompleteResponse(query: String) {
        viewModelScope.launch {
            _autoCompleteResponse.value = historicalWeatherRepo.getAutoComplete(query)
        }
    }
}