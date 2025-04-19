package com.example.weatherapp.di

import com.example.weatherapp.network.CurrentWeatherRepo
import com.example.weatherapp.network.HistoricalWeatherRepo
import com.example.weatherapp.network.MyForecastRepo
import com.example.weatherapp.network.SavedLocationsRepo
import com.example.weatherapp.network.Service
import com.example.weatherapp.room.BookmarkDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@Module
@InstallIn(SingletonComponent::class)
class RepoModule {

    @Provides
    fun provideIoDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Provides
    fun provideCurrentRepo(
        service: Service,
        ioDispatcher: CoroutineDispatcher,
        bookmarkDao: BookmarkDao
    ): CurrentWeatherRepo {
        return CurrentWeatherRepo(
            service,
            ioDispatcher,
            bookmarkDao
        )
    }

    @Provides
    fun provideHistoricalWeatherRepo(
        service: Service,
        ioDispatcher: CoroutineDispatcher
    ): HistoricalWeatherRepo {
        return HistoricalWeatherRepo(
            service,
            ioDispatcher
        )
    }

    @Provides
    fun provideSavedLocationsRepo(
        service: Service,
        ioDispatcher: CoroutineDispatcher,
        bookmarkDao: BookmarkDao
    ): SavedLocationsRepo {
        return SavedLocationsRepo(
            service = service,
            ioDispatcher = ioDispatcher,
            bookmarkDao = bookmarkDao
        )
    }

    @Provides
    fun provideMyForecastRepo(
        service: Service,
        ioDispatcher: CoroutineDispatcher
    ): MyForecastRepo {
        return MyForecastRepo(
            service,
            ioDispatcher
        )
    }
}