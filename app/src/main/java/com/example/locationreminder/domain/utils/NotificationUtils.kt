package com.example.locationreminder.domain.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.locationreminder.R
import com.example.locationreminder.domain.model.Reminder
import com.example.locationreminder.presentation.MainActivity

class NotificationUtils {

    fun createChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                context.getString(R.string.channel_geofence_id),
                context.getString(R.string.channel_geofence_name),
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                enableLights(true)
                lightColor = Color.GREEN
                enableVibration(true)
                description = context.getString(R.string.channel_geofence_description)
            }

            val notificationManager = context.getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }
}

fun NotificationManager.sendGeofenceNotification(context: Context, reminder: Reminder) {
    val pendingIntent = TaskStackBuilder.create(context).run {
        addNextIntentWithParentStack(Intent(context, MainActivity::class.java))
        getPendingIntent(0, PendingIntent.FLAG_IMMUTABLE)
    }


    val notificationBuilder = NotificationCompat.Builder(
        context,
        context.getString(R.string.channel_geofence_id)
    ).setStyle(
        NotificationCompat.BigTextStyle()
            .bigText(reminder.description)
            .setBigContentTitle(reminder.title)
    ).setSmallIcon(R.drawable.ic_location_black)
        .setContentTitle(reminder.title)
        .setContentText(reminder.description)
        .setContentIntent(pendingIntent)
        .setAutoCancel(true)

    notify(0, notificationBuilder.build())
}