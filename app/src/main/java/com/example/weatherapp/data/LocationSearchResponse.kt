package com.example.weatherapp.data

data class LocationSearchResponse(
    val request: LocationRequest,
    val results: List<LocationResult>
)

