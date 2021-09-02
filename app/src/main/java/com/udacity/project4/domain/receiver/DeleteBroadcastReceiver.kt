package com.udacity.project4.domain.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.udacity.project4.domain.worker.DeleteWorker

class DeleteBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val id = intent.getStringExtra("id")
        val workData = workDataOf("id" to id)
        val deleteWorkRequest = OneTimeWorkRequestBuilder<DeleteWorker>()
            .setInputData(workData)
            .build()
        WorkManager.getInstance(context).enqueueUniqueWork(
            "DeleteWork",
            ExistingWorkPolicy.APPEND_OR_REPLACE,
            deleteWorkRequest
        )
    }
}