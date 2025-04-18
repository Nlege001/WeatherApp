package com.example.weatherapp.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface BookmarkDao {

    @Query("SELECT * FROM locations")
    fun getSavedLocations(): Flow<List<BookmarkedLocationsEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addLocation(location: BookmarkedLocationsEntity)

    @Query("DELETE FROM locations WHERE locationName = :name")
    suspend fun deleteLocation(name: String)

    @Query("SELECT EXISTS(SELECT 1 FROM locations WHERE locationName = :name)")
    fun isLocationBookmarked(name: String): Flow<Boolean>
}