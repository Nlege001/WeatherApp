package com.example.weatherapp.data

data class SavedLocationData(
    val locationName: String,
    val iconUrl: String,
    val temperature: Int,
    val feelsLike: Int,
)