package com.udacity.project4.data.repository

import com.udacity.project4.data.cache.database.ReminderDao
import com.udacity.project4.data.cache.model.toReminder
import com.udacity.project4.domain.model.Reminder
import com.udacity.project4.domain.model.toReminderEntity
import javax.inject.Inject

class ReminderRepository @Inject constructor(
    val reminderDao: ReminderDao
) {
    val reminderList = reminderDao.getAllReminders()

    suspend fun insertReminder(reminder: Reminder) {
        reminderDao.insertReminder(reminder.toReminderEntity())
    }

    suspend fun updateReminder(reminder: Reminder) {
        reminderDao.updateReminder(reminder.toReminderEntity())
    }

    suspend fun getReminder(id: String): Reminder {
        return reminderDao.getReminder(id).toReminder()
    }

    suspend fun deleteReminder(id: String) {
        reminderDao.deleteReminder(id)
    }
}