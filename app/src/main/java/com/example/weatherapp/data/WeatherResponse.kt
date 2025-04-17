package com.example.weatherapp.data

data class WeatherResponse(
    val request: Request,
    val location: Location,
    val current: Current
)

