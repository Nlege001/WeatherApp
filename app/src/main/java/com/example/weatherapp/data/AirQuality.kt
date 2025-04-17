package com.example.weatherapp.data

data class AirQuality(
    val co: String,
    val no2: String,
    val o3: String,
    val so2: String,
    val pm2_5: String,
    val pm10: String,
    val `us-epa-index`: String,
    val `gb-defra-index`: String
)