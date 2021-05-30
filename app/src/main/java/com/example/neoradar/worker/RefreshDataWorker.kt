package com.example.neoradar.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.neoradar.database.getDatabase
import com.example.neoradar.network.calculateCurrentDate
import com.example.neoradar.repository.NeoRepository
import java.util.*

class RefreshDataWorker(appContext: Context, params: WorkerParameters) :
    CoroutineWorker(appContext, params) {

    companion object {
        const val WORK_NAME = "RefreshDataWorker"
    }

    /**
     * Loads new data into the database.  Calculates current date and deletes any info from both the
     * neoDatabase and imageOfTheDayDatabase that has a date earlier than the calculated date.
     */
    override suspend fun doWork(): Result {
        val database = getDatabase(applicationContext)
        val repository = NeoRepository(database)

        return try {
            repository.refreshNeoData()

            val calendar = Calendar.getInstance()
            val currentDate = calculateCurrentDate(calendar)
            repository.deleteNeoDatabase(currentDate)
            repository.deleteImageOfTheDay(currentDate)
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
}