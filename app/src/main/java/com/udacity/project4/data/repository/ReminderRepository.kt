package com.udacity.project4.data.repository

import com.udacity.project4.data.cache.model.ReminderEntity
import com.udacity.project4.domain.model.Reminder
import kotlinx.coroutines.flow.Flow

interface ReminderRepository {

    val reminderList: Flow<List<ReminderEntity>>

    suspend fun insertReminder(reminder: Reminder)

    suspend fun updateReminder(reminder: Reminder)

    suspend fun getReminder(id: String): Reminder?

    suspend fun deleteReminder(id: String)
}