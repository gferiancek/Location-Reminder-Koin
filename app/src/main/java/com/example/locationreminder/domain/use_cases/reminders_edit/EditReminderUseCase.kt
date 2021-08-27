package com.example.locationreminder.domain.use_cases.reminders_edit

import com.example.locationreminder.cache.database.ReminderDao
import com.example.locationreminder.domain.DataState
import com.example.locationreminder.domain.Reminder
import com.example.locationreminder.domain.toReminderEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class EditReminderUseCase(
    private val reminderDao: ReminderDao
) {
    fun execute(
        reminder: Reminder
    ): Flow<DataState<Reminder>> = flow {
        try {
            emit(DataState.loading())
            if (reminder.missingData()) {
                emit(DataState.error<Reminder>(message = "Please fill out all fields before submitting reminder"))
            } else {
                reminderDao.updateReminder(reminder.toReminderEntity())
                emit(DataState.data(reminder))
            }
        } catch (e: Exception) {
            emit(DataState.error<Reminder>(message = e.message.toString()))
        }
    }
}
