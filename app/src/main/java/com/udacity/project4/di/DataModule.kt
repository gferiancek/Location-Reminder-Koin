package com.udacity.project4.di

import androidx.room.Room
import com.udacity.project4.ReminderApplication
import com.udacity.project4.data.cache.database.ReminderDao
import com.udacity.project4.data.cache.database.ReminderDatabase
import com.udacity.project4.data.repository.ReminderRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

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

    @Singleton
    @Provides
    fun provideReminderRepository(dao: ReminderDao): ReminderRepositoryImpl {
        return ReminderRepositoryImpl(dao)
    }
}