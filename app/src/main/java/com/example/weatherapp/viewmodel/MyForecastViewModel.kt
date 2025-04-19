package com.example.weatherapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.CallState
import com.example.weatherapp.data.MarineForecastResponse
import com.example.weatherapp.network.MyForecastRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyForecastViewModel @Inject constructor(
    private val repo: MyForecastRepo
) : ViewModel() {

    private val _forecast =
        MutableStateFlow<CallState<MarineForecastResponse>>(CallState.EmptyContent)
    val forecast: StateFlow<CallState<MarineForecastResponse>> = _forecast

    fun getForecast(
        lat: String,
        long: String,
    ) {
        viewModelScope.launch {
            _forecast.value = CallState.Loading
            _forecast.value = repo.getLocationForeCast(
                lat = lat,
                long = long,
            )
        }
    }
}