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
            when {
                reminder.hasBlankTextFields() -> {
                    emit(DataState.error(message = "Please fill out all text fields before submitting reminder"))
                }
                else -> {
                    reminderDao.updateReminder(reminder.toReminderEntity())
                    emit(DataState.data(reminder))
                }
            }
        } catch (e: Exception) {
            emit(DataState.error(message = e.message.toString()))
        }
    }
}
