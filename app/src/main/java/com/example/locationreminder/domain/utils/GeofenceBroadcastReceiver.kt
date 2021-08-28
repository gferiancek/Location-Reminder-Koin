package com.example.locationreminder.domain.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.work.*
import com.google.android.gms.location.GeofenceStatusCodes
import com.google.android.gms.location.GeofencingEvent
import java.util.*

class GeofenceBroadcastReceiver(
) : BroadcastReceiver() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onReceive(context: Context, intent: Intent) {

        val geofencingEvent = GeofencingEvent.fromIntent(intent)
        if (geofencingEvent.hasError()) {
            val error = GeofenceStatusCodes.getStatusCodeString(geofencingEvent.errorCode)
            Log.d("BroadcastReceiver", error)
            return
        }
        val geofenceTransition = geofencingEvent.geofenceTransition
        val triggeringGeofence = geofencingEvent.triggeringGeofences
        if (triggeringGeofence.isNotEmpty()) {
            val workData = workDataOf(
                "geofenceId" to triggeringGeofence[0].requestId,
                "geofenceTransition" to geofenceTransition
            )

            val geofenceWorkRequest: OneTimeWorkRequest =
                OneTimeWorkRequestBuilder<GeofenceWorker>()
                    .setInputData(workData)
                    .build()
            WorkManager.getInstance(context).enqueueUniqueWork(
                UUID.randomUUID().toString(),
                ExistingWorkPolicy.REPLACE,
                geofenceWorkRequest
            )
        }
    }
}