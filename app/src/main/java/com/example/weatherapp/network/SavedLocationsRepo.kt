package com.example.weatherapp.network

import com.example.weatherapp.data.CallState
import com.example.weatherapp.data.SavedLocationData
import com.example.weatherapp.room.BookmarkDao
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import javax.inject.Inject

@ViewModelScoped
class SavedLocationsRepo @Inject constructor(
    private val service: Service,
    private val bookmarkDao: BookmarkDao,
    private val ioDispatcher: CoroutineDispatcher
) {
    suspend fun getLocations(): CallState<List<SavedLocationData>> = withContext(ioDispatcher) {
        return@withContext try {
            val savedLocations = bookmarkDao.getSavedLocations().first()

            val results = savedLocations.mapNotNull { entity ->
                val response = service.getCurrentWeather(entity.locationName)

                if (response.isSuccessful) {
                    response.body()?.let { body ->
                        SavedLocationData(
                            locationName = body.location.name,
                            iconUrl = body.current.weather_icons.firstOrNull() ?: "",
                            temperature = body.current.temperature,
                            feelsLike = body.current.feelslike
                        )
                    }
                } else null
            }

            CallState.Content(results)
        } catch (e: Exception) {
            CallState.Error
        }
    }
}