package com.udacity.project4

import android.app.Application
import androidx.work.Configuration
import com.udacity.project4.di.*
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.workmanager.koin.workManagerFactory
import org.koin.core.KoinExperimentalAPI
import org.koin.core.context.startKoin

@KoinExperimentalAPI
class ReminderApplication : Application(), Configuration.Provider {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@ReminderApplication)
            modules(
                listOf(
                    dataModule,
                    useCasesModule,
                    viewModelModule,
                    workerModule
                )
            )
            workManagerFactory()
        }
    }

    override fun getWorkManagerConfiguration() =
        Configuration.Builder()
            .setMinimumLoggingLevel(android.util.Log.DEBUG)
            .build()
}