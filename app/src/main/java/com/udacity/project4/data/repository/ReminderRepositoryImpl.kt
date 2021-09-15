package com.udacity.project4.data.repository

import com.udacity.project4.data.cache.database.ReminderDao
import com.udacity.project4.data.cache.model.toReminder
import com.udacity.project4.domain.model.Reminder
import com.udacity.project4.domain.model.toReminderEntity
import com.udacity.project4.util.wrapEspressoIdlingResource
import javax.inject.Inject

class ReminderRepositoryImpl @Inject constructor(
    val reminderDao: ReminderDao
) : ReminderRepository {
    override val reminderList = reminderDao.getAllReminders()

    override suspend fun insertReminder(reminder: Reminder) {
        wrapEspressoIdlingResource {
            reminderDao.insertReminder(reminder.toReminderEntity())
        }
    }

    override suspend fun updateReminder(reminder: Reminder) {
        wrapEspressoIdlingResource {
            reminderDao.updateReminder(reminder.toReminderEntity())
        }
    }

    override suspend fun getReminder(id: String): Reminder? {
        wrapEspressoIdlingResource {
            return reminderDao.getReminder(id)?.toReminder()
        }
    }

    override suspend fun deleteReminder(id: String) {
        wrapEspressoIdlingResource {
            reminderDao.deleteReminder(id)
        }
    }

    override suspend fun deleteAllReminder() {
        wrapEspressoIdlingResource {
            reminderDao.deleteAllReminders()
        }
    }
}