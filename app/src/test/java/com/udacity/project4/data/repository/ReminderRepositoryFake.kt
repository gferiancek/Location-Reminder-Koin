package com.udacity.project4.data.repository

import com.udacity.project4.data.cache.database.ReminderDaoFake
import com.udacity.project4.data.cache.model.toReminder
import com.udacity.project4.domain.model.Reminder
import com.udacity.project4.domain.model.toReminderEntity

class ReminderRepositoryFake(
    private val reminderDao: ReminderDaoFake
) : ReminderRepository {

    override val reminderList = reminderDao.getAllReminders()

    override suspend fun insertReminder(reminder: Reminder) {
        reminderDao.insertReminder(reminder.toReminderEntity())
    }

    override suspend fun updateReminder(reminder: Reminder) {
        reminderDao.updateReminder(reminder.toReminderEntity())
    }

    override suspend fun getReminder(id: String): Reminder? {
        return reminderDao.getReminder(id)?.toReminder()
    }

    override suspend fun deleteReminder(id: String) {
        reminderDao.deleteReminder(id)
    }
}