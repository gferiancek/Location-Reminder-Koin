package com.example.locationreminder.di

import androidx.room.Room
import com.example.locationreminder.ReminderApplication
import com.example.locationreminder.cache.database.ReminderDao
import com.example.locationreminder.cache.database.ReminderDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CacheModule {

    @Singleton
    @Provides
    fun provideDatabase(app: ReminderApplication): ReminderDatabase {
        return Room
            .databaseBuilder(
                app,
                ReminderDatabase::class.java,
                ReminderDatabase.DATABASE_NAME
            )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideReminderDao(database: ReminderDatabase): ReminderDao {
        return database.reminderDao()
    }
}