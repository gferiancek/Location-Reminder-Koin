package com.udacity.project4.domain.worker

import android.app.NotificationManager
import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.udacity.project4.data.repository.ReminderRepository
import com.udacity.project4.domain.model.Reminder
import com.udacity.project4.domain.utils.sendGeofenceNotification
import com.udacity.project4.presentation.ui.reminders.reminders_edit.MapUtils
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class GeofenceWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    val repository: ReminderRepository,
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        val geofenceId = inputData.getString("geofenceId")
        var reminder: Reminder
        geofenceId?.let { id ->
            reminder = repository.getReminder(id)
            val geofenceTransition = inputData.getInt("geofenceTransition", -1)
            val reminderTransition = MapUtils.getTransitionConstant(reminder.transition_type)
            if (geofenceTransition == reminderTransition) {
                val notificationManager =
                    applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.sendGeofenceNotification(applicationContext, reminder)
                return Result.success()
            }
        }
        return Result.retry()
    }
}