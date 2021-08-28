package com.example.locationreminder.domain.utils

import android.app.NotificationManager
import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.locationreminder.cache.database.ReminderDao
import com.example.locationreminder.cache.model.toReminder
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class GeofenceWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    val reminderDao: ReminderDao,
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        val geofenceId = inputData.getString("geofenceId")
        val geofenceTransition = inputData.getInt("geofenceTransition", -1)
        try {
            val reminder = geofenceId?.let { reminderDao.getReminder(geofenceId) }?.toReminder()
            reminder?.let {
                if (reminder.transitionType == geofenceTransition) {
                    val notificationManager =
                        applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                    notificationManager.sendGeofenceNotification(applicationContext, reminder)
                    return Result.success()
                }
            }
        } catch (e: Exception) {
            Log.d("GeofenceWorker", e.toString())
            return Result.retry()
        }
        return Result.retry()
    }
}