package com.udacity.project4.data.cache

import com.udacity.project4.data.cache.database.ReminderDao
import com.udacity.project4.data.cache.model.ReminderEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class ReminderDaoFake(
    private val databaseFake: ReminderDatabaseFake
) : ReminderDao {
    override suspend fun insertReminder(reminder: ReminderEntity) {
        databaseFake.reminders.add(reminder)
    }

    override suspend fun updateReminder(reminder: ReminderEntity): Int {
        val oldReminder = databaseFake.reminders.find { it.id == reminder.id }
        oldReminder?.let {
            val index = databaseFake.reminders.indexOf(oldReminder)
            databaseFake.reminders.removeAt(index)
            databaseFake.reminders.add(index, reminder)
            return 1
        }
        return 0
    }

    override fun getAllReminders(): Flow<List<ReminderEntity>> {
        return flowOf(databaseFake.reminders)
    }

    override suspend fun getReminder(id: String): ReminderEntity? {
        return databaseFake.reminders.find { it.id == id }
    }

    override suspend fun deleteReminder(id: String) {
        val reminder = databaseFake.reminders.find { it.id == id }
        reminder?.let {
            databaseFake.reminders.remove(reminder)
        }
    }

    override suspend fun deleteAllReminders() {
        TODO("Not yet implemented")
    }
}