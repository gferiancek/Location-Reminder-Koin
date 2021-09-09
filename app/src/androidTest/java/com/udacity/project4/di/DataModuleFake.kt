package com.udacity.project4.di

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.udacity.project4.data.cache.database.ReminderDao
import com.udacity.project4.data.cache.database.ReminderDatabase
import com.udacity.project4.data.repository.ReminderRepository
import com.udacity.project4.data.repository.ReminderRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DataModule::class]
)
object DataModuleFake {

    @Singleton
    @Provides
    fun provideDatabase() = Room.inMemoryDatabaseBuilder(
        ApplicationProvider.getApplicationContext(),
        ReminderDatabase::class.java
    ).build()

    @Singleton
    @Provides
    fun provideReminderDao(database: ReminderDatabase): ReminderDao {
        return database.reminderDao()
    }

    @Singleton
    @Provides
    fun provideReminderRepository(dao: ReminderDao): ReminderRepository {
        return ReminderRepositoryImpl(dao)
    }
}