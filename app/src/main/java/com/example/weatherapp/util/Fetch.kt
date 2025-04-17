package com.example.weatherapp.util

import android.util.Log
import com.example.weatherapp.data.CallState
import retrofit2.Response

suspend fun <T> fetch(api: suspend () -> Response<T>): CallState<T> {
    return try {
        Log.d("NetworkCall", "Calling API...")
        val response = api()
        Log.d("NetworkCall", "API response received. Success: ${response.isSuccessful}")

        if (response.isSuccessful) {
            response.body()?.let {
                Log.d("NetworkCall", "API returned valid body: $it")
                CallState.Content(it)
            } ?: run {
                Log.e("NetworkCall", "Response body is null")
                CallState.Error
            }
        } else {
            Log.e(
                "NetworkCall",
                "API call failed with code: ${response.code()}, message: ${response.message()}"
            )
            CallState.Error
        }
    } catch (e: Exception) {
        Log.e("NetworkCall", "API call threw an exception: ${e.message}", e)
        CallState.Error
    }
}