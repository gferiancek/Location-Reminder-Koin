package com.example.locationreminder

import android.app.Application
import androidx.work.Configuration
import com.example.locationreminder.domain.utils.GeofenceWorkerFactory
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class ReminderApplication : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: GeofenceWorkerFactory

    override fun getWorkManagerConfiguration() =
        Configuration.Builder()
            .setMinimumLoggingLevel(android.util.Log.DEBUG)
            .setWorkerFactory(workerFactory)
            .build()
}