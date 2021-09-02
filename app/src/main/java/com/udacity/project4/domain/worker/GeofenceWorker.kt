package com.udacity.project4.domain.worker

import android.app.NotificationManager
import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.udacity.project4.cache.database.ReminderDao
import com.udacity.project4.cache.model.toReminder
import com.udacity.project4.domain.model.Reminder
import com.udacity.project4.domain.utils.sendGeofenceNotification
import com.udacity.project4.presentation.ui.reminders.reminders_edit.MapUtils
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
        var reminder: Reminder
        geofenceId?.let { id ->
            reminder = reminderDao.getReminder(id).toReminder()
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