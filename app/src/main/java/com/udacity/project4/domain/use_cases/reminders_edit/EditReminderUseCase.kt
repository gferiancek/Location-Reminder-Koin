package com.udacity.project4.domain.use_cases.reminders_edit

import com.udacity.project4.cache.database.ReminderDao
import com.udacity.project4.domain.model.DataState
import com.udacity.project4.domain.model.Reminder
import com.udacity.project4.domain.model.toReminderEntity
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
