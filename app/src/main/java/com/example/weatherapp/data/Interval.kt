package com.example.weatherapp.data

enum class Interval(val label: String, val value: Int) {
    ONE(label = "1 hr", value = 1),
    THREE(label = "3 hrs", value = 3),
    SIX(label = "6 hrs", value = 6),
    TWELVE(label = "12 hrs", value = 12),
    TWENTY_FOUR_HOURS(label = "24 hrs", value = 24),
}