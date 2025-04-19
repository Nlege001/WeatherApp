package com.example.weatherapp.data

data class MarineForecastResponse(
    val request: RequestInfo,
    val forecast: List<ForecastDay>
)

