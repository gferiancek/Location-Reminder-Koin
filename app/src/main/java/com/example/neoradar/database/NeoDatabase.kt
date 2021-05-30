package com.example.neoradar.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [NeoEntity::class, ImageOfTheDayEntity::class],
    version = 1,
    exportSchema = false
)
abstract class NeoDatabase : RoomDatabase() {
    abstract val neoDao: NeoDao
}

private lateinit var INSTANCE: NeoDatabase

fun getDatabase(context: Context): NeoDatabase {
    synchronized(NeoDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(
                context.applicationContext,
                NeoDatabase::class.java,
                "neo_database"
            )
                .fallbackToDestructiveMigration()
                .build()
        }
    }
    return INSTANCE
}