package com.example.weatherapp.data

data class Current(
    val observation_time: String,
    val temperature: Int,
    val weather_code: Int,
    val weather_icons: List<String>,
    val weather_descriptions: List<String>,
    val astro: Astro,
    val air_quality: AirQuality,
    val wind_speed: Int,
    val wind_degree: Int,
    val wind_dir: String,
    val pressure: Int,
    val precip: Int,
    val humidity: Int,
    val cloudcover: Int,
    val feelslike: Int,
    val uv_index: Int,
    val visibility: Int,
    val is_day: String
)