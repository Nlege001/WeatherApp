package com.example.weatherapp.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "locations")
data class BookmarkedLocationsEntity(
    @PrimaryKey val locationName: String
)