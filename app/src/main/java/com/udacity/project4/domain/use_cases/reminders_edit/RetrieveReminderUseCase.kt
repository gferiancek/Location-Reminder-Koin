package com.udacity.project4.domain.use_cases.reminders_edit

import com.udacity.project4.cache.database.ReminderDao
import com.udacity.project4.cache.model.toReminder
import com.udacity.project4.domain.model.DataState
import com.udacity.project4.domain.model.Reminder
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class RetrieveReminderUseCase(
    private val reminderDao: ReminderDao
) {
    fun execute(
        reminderId: String
    ): Flow<DataState<Reminder>> = flow {
        try {
            emit(DataState.loading())
            val currentReminder = reminderDao.getReminder(reminderId).toReminder()
            emit(DataState.data(currentReminder))
        } catch (e: Exception) {
            emit(DataState.error<Reminder>(message = e.message.toString()))
        }
    }
}
