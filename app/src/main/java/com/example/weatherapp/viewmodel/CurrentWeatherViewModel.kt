package com.example.weatherapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.CallState
import com.example.weatherapp.data.LocationSearchResponse
import com.example.weatherapp.data.WeatherResponse
import com.example.weatherapp.network.CurrentWeatherRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CurrentWeatherViewModel @Inject constructor(
    private val repo: CurrentWeatherRepo,
) : ViewModel() {

    private val _currentWeatherResponse =
        MutableStateFlow<CallState<WeatherResponse>>(CallState.EmptyContent)
    val currentWeatherResponse: StateFlow<CallState<WeatherResponse>> = _currentWeatherResponse

    private val _autoCompleteResponse =
        MutableStateFlow<LocationSearchResponse?>(null)
    val autoCompleteResponse: StateFlow<LocationSearchResponse?> = _autoCompleteResponse

    private val _isLocationBookmarked = MutableStateFlow<Boolean>(false)
    val isLocationBookmarked: StateFlow<Boolean> = _isLocationBookmarked

    fun getCurrentWeather(userInput: String) {
        viewModelScope.launch {
            _isLocationBookmarked.value = repo.isLocationBookmarked(userInput).first()
            _currentWeatherResponse.value = CallState.Loading
            _currentWeatherResponse.value = repo.getCurrentWeather(userInput)
        }
    }

    fun getAutoCompleteResponse(query: String) {
        viewModelScope.launch {
            _autoCompleteResponse.value = repo.getAutoComplete(query)
        }
    }

    fun evalLocationBookmark(location: String) {
        viewModelScope.launch {
            repo.evalLocationBookmark(location)
            _isLocationBookmarked.value = repo.isLocationBookmarked(location).first()
        }
    }
}