package com.example.weatherapp.network

import com.example.weatherapp.data.CallState
import com.example.weatherapp.data.LocationSearchResponse
import com.example.weatherapp.data.WeatherResponse
import com.example.weatherapp.room.BookmarkDao
import com.example.weatherapp.room.BookmarkedLocationsEntity
import com.example.weatherapp.util.fetch
import com.example.weatherapp.util.fetchData
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject

@ViewModelScoped
class CurrentWeatherRepo @Inject constructor(
    private val service: Service,
    private val ioDispatcher: CoroutineDispatcher,
    private val bookmarkDao: BookmarkDao
) {
    suspend fun getCurrentWeather(
        state: String,
    ): CallState<WeatherResponse> {
        return fetch(
            ioDispatcher = ioDispatcher,
            api = { service.getCurrentWeather(state) }
        )
    }

    suspend fun getAutoComplete(
        query: String
    ): LocationSearchResponse? {
        return fetchData(
            ioDispatcher = ioDispatcher,
            api = { service.getAutoComplete(query) }
        )
    }

    fun isLocationBookmarked(location: String): Flow<Boolean> {
        return bookmarkDao.isLocationBookmarked(location)
    }

    suspend fun evalLocationBookmark(location: String) {
        val entity = BookmarkedLocationsEntity(location)
        if (bookmarkDao.isLocationBookmarked(location).first()) {
            bookmarkDao.deleteLocation(location)
        } else {
            bookmarkDao.addLocation(entity)
        }
    }
}