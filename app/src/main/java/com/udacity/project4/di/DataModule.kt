package com.udacity.project4.di

import android.app.Application
import androidx.room.Room
import com.udacity.project4.data.cache.database.ReminderDao
import com.udacity.project4.data.cache.database.ReminderDatabase
import com.udacity.project4.data.repository.ReminderRepository
import com.udacity.project4.data.repository.ReminderRepositoryImpl
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val dataModule = module {

    fun provideDatabase(app: Application): ReminderDatabase {
        return Room.databaseBuilder(
            app,
            ReminderDatabase::class.java,
            ReminderDatabase.DATABASE_NAME
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    fun provideReminderDao(database: ReminderDatabase): ReminderDao {
        return database.reminderDao()
    }

    single { provideDatabase(androidApplication()) }
    single { provideReminderDao(get()) }
    single<ReminderRepository> { ReminderRepositoryImpl(get()) }
}