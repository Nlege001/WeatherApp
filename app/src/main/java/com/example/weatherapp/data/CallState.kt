package com.example.weatherapp.data

sealed class CallState<out T> {
    object Loading : CallState<Nothing>()

    object Error : CallState<Nothing>()

    data class Content<out T>(
        val data: T
    ) : CallState<T>()
}