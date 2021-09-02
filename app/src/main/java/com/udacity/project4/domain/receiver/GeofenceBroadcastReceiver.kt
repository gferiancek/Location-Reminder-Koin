package com.udacity.project4.domain.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofenceStatusCodes
import com.google.android.gms.location.GeofencingEvent
import com.udacity.project4.domain.worker.GeofenceWorker

class GeofenceBroadcastReceiver(
) : BroadcastReceiver() {
    private val TAG = "GeofenceBroadcastReceiver"
    override fun onReceive(context: Context, intent: Intent) {
        val geofencingEvent = GeofencingEvent.fromIntent(intent)
        if (geofencingEvent.hasError()) {
            val error = GeofenceStatusCodes.getStatusCodeString(geofencingEvent.errorCode)
            Log.d(TAG, error)
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
                    "geofenceTransition" to geofenceTransition
                )

                val geofenceWorkRequest = OneTimeWorkRequestBuilder<GeofenceWorker>()
                    .setInputData(workData)
                    .build()
                WorkManager.getInstance(context).enqueueUniqueWork(
                    "GeofenceLocationWorker",
                    ExistingWorkPolicy.REPLACE,
                    geofenceWorkRequest
                )
            }
        }
    }
}