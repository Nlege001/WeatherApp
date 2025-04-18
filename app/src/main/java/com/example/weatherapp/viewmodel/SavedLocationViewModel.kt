package com.example.weatherapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.CallState
import com.example.weatherapp.data.SavedLocationData
import com.example.weatherapp.network.SavedLocationsRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SavedLocationViewModel @Inject constructor(
    private val repo: SavedLocationsRepo
) : ViewModel() {

    private val _savedLocations =
        MutableStateFlow<CallState<List<SavedLocationData>>>(CallState.EmptyContent)
    val savedLocations: StateFlow<CallState<List<SavedLocationData>>> = _savedLocations

    fun getSavedLocations() {
        viewModelScope.launch {
            _savedLocations.value = CallState.Loading
            _savedLocations.value = repo.getLocations()
        }
    }

    fun removeLocation(location: String) {
        viewModelScope.launch {
            repo.removeLocation(location)
            _savedLocations.value = repo.getLocations()
        }
    }
}