package com.udacity.project4.domain.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.work.*
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofenceStatusCodes
import com.google.android.gms.location.GeofencingEvent

class GeofenceBroadcastReceiver(
) : BroadcastReceiver() {
    val TAG = "BroadcastReceiver"
    override fun onReceive(context: Context, intent: Intent) {
        Log.d(TAG, "Receiver has fired")
        val geofencingEvent = GeofencingEvent.fromIntent(intent)
        if (geofencingEvent.hasError()) {
            val error = GeofenceStatusCodes.getStatusCodeString(geofencingEvent.errorCode)
            Log.d("BroadcastReceiver", error)
            return
        }
        val geofenceTransition = geofencingEvent.geofenceTransition
        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER ||
            geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT ||
            geofenceTransition == Geofence.GEOFENCE_TRANSITION_DWELL
        ) {
            val triggeringGeofence = geofencingEvent.triggeringGeofences
            if (triggeringGeofence.isNotEmpty()) {
                val workData = workDataOf(
                    "geofenceId" to triggeringGeofence[0].requestId,
                )

                val geofenceWorkRequest: OneTimeWorkRequest =
                    OneTimeWorkRequestBuilder<GeofenceWorker>()
                        .setInputData(workData)
                        .build()
                WorkManager.getInstance(context).enqueueUniqueWork(
                    "GeofenceLocationWorker",
                    ExistingWorkPolicy.REPLACE,
                    geofenceWorkRequest
                )
                Log.d(TAG, "Enqueued Unique Work")
            }
        }
    }
}