package com.udacity.project4.domain.worker

import android.app.NotificationManager
import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.android.gms.location.LocationServices
import com.udacity.project4.data.repository.ReminderRepository

class DeleteWorker(
    appContext: Context,
    workerParams: WorkerParameters,
    val repository: ReminderRepository,
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        val id = inputData.getString("id")
        id?.let {
            repository.deleteReminder(id)
            val geofenceClient = LocationServices.getGeofencingClient(applicationContext)
            geofenceClient.removeGeofences(listOf(id))
            applicationContext.getSystemService(NotificationManager::class.java).cancel(0)
            return Result.success()
        }
        return Result.retry()
    }
}