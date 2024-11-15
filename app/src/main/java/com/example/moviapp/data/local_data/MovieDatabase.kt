package com.example.moviapp.data.local_data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [MovieEntity::class],
    version = 2,
    exportSchema = false
)
abstract class MovieDatabase : RoomDatabase(){
    abstract val movieDao : MovieDao
}