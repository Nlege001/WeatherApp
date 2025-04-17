package com.example.weatherapp.data

data class Location(
    val name: String,
    val country: String,
    val region: String,
    val lat: String,
    val lon: String,
    val timezone_id: String,
    val localtime: String,
    val localtime_epoch: Long,
    val utc_offset: String
)