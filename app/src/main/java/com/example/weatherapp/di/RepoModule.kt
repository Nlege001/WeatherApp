package com.example.weatherapp.di

import com.example.weatherapp.network.CurrentWeatherRepo
import com.example.weatherapp.network.Service
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
        ioDispatcher: CoroutineDispatcher
    ): CurrentWeatherRepo {
        return CurrentWeatherRepo(
            service,
            ioDispatcher
        )
    }
}