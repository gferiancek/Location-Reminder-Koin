package com.udacity.project4.domain.worker

import android.app.NotificationManager
import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.udacity.project4.data.repository.ReminderRepository
import com.udacity.project4.domain.utils.sendGeofenceNotification
import com.udacity.project4.presentation.ui.reminders.reminders_edit.MapUtils

class GeofenceWorker(
    appContext: Context,
    workerParams: WorkerParameters,
    val repository: ReminderRepository,
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        val geofenceId = inputData.getString("geofenceId")
        geofenceId?.let { id ->
            val currentReminder = repository.getReminder(id)
            if (currentReminder != null) {
                val geofenceTransition = inputData.getInt("geofenceTransition", -1)
                val reminderTransition =
                    MapUtils.getTransitionConstant(currentReminder.transition_type)
                if (geofenceTransition == reminderTransition) {
                    val notificationManager =
                        applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                    notificationManager.sendGeofenceNotification(
                        applicationContext,
                        currentReminder
                    )
                    return Result.success()
                }
            }
        }
        return Result.retry()
    }
}