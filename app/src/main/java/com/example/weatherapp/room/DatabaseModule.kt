package com.example.weatherapp.room

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Provides
    fun provideDatabase(@ApplicationContext context: Context): AppDb {
        return Room.databaseBuilder(
            context,
            AppDb::class.java,
            "app_db"
        ).fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideWorkoutDao(appDb: AppDb): BookmarkDao {
        return appDb.bookmarksDao()
    }
}