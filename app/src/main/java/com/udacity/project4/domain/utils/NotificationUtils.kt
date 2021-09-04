package com.udacity.project4.domain.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.core.app.NotificationCompat
import androidx.navigation.NavDeepLinkBuilder
import com.udacity.project4.R
import com.udacity.project4.domain.model.Reminder
import com.udacity.project4.domain.receiver.DeleteBroadcastReceiver

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
    val bundle = Bundle()
    bundle.putParcelable("currentReminder", reminder)
    bundle.putString("label", reminder.location_name)
    val pendingIntent = NavDeepLinkBuilder(context)
        .setGraph(R.navigation.nav_graph)
        .setDestination(R.id.reminderDetailFragment)
        .setArguments(bundle)
        .createPendingIntent()

    val deleteIntent = Intent(context, DeleteBroadcastReceiver::class.java)
    deleteIntent.putExtra("id", reminder.id)
    val deletePendingIntent = PendingIntent.getBroadcast(
        context,
        0,
        deleteIntent,
        PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
    )

    val notification = NotificationCompat.Builder(
        context,
        context.getString(R.string.channel_geofence_id)
    ).setSmallIcon(R.drawable.ic_location_black)
        .setContentTitle(reminder.title)
        .setContentText(reminder.location_name)
        .setStyle(
            NotificationCompat.BigTextStyle()
                .bigText(reminder.description)
        )
        .setContentIntent(pendingIntent)
        .addAction(
            R.drawable.ic_baseline_delete_24,
            context.getString(R.string.notification_delete),
            deletePendingIntent
        )
        .setAutoCancel(true)
        .build()

    notify(0, notification)
}