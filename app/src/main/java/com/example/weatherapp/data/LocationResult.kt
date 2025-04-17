package com.example.weatherapp.data

data class LocationResult(
    val name: String,
    val country: String,
    val region: String,
    val lon: String,
    val lat: String,
    val timezone_id: String,
    val utc_offset: String
)