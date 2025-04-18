package com.example.weatherapp.data

data class WeatherStackResponse(
    val request: Request,
    val location: Location,
    val current: Current,
    val historical: Map<String, HistoricalDay>?
)