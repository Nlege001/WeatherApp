package com.example.weatherapp.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [BookmarkedLocationsEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDb : RoomDatabase() {
    abstract fun bookmarksDao(): BookmarkDao
}