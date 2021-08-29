package com.udacity.project4.domain.utils

import android.app.NotificationManager
import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.udacity.project4.cache.database.ReminderDao
import com.udacity.project4.cache.model.toReminder
import com.udacity.project4.domain.model.Reminder
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class GeofenceWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    val reminderDao: ReminderDao,
) : CoroutineWorker(appContext, workerParams) {

    private val TAG = "GeofenceWorker"
    override suspend fun doWork(): Result {
        Log.d(TAG, "doWork fired")
        try {
            Log.d(TAG, "Try block entered")
            val geofenceId = inputData.getString("geofenceId")
            var reminder: Reminder
            geofenceId?.let {
                Log.d(TAG, "geofenceId isn't null")
                reminder = reminderDao.getReminder(it).toReminder()
                Log.d(TAG, "Retrieved reminder from cache")
                val notificationManager =
                    applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.sendGeofenceNotification(applicationContext, reminder)
                Log.d(TAG, "Notification sent")
                return Result.success()
            }
        } catch (e: Exception) {
            Log.d(TAG, e.toString())
        }
        Log.d(TAG, "Worker failed, no exception caught")
        return Result.retry()
    }
}