package com.udacity.project4.domain.worker

import android.app.NotificationManager
import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.udacity.project4.data.repository.ReminderRepositoryImpl
import com.udacity.project4.domain.utils.sendGeofenceNotification
import com.udacity.project4.presentation.ui.reminders.reminders_edit.MapUtils
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class GeofenceWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    val repository: ReminderRepositoryImpl,
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