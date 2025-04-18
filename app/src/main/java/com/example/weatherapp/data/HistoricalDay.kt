package com.example.weatherapp.data

data class HistoricalDay(
    val date: String,
    val date_epoch: Long,
    val astro: Astro,
    val mintemp: Int,
    val maxtemp: Int,
    val avgtemp: Int,
    val totalsnow: Int,
    val sunhour: Double,
    val uv_index: Int,
    val hourly: List<HourlyWeather>
)