package com.example.locationreminder.domain.use_cases.reminders_edit

import com.example.locationreminder.cache.database.ReminderDao
import com.example.locationreminder.cache.model.toReminder
import com.example.locationreminder.domain.model.DataState
import com.example.locationreminder.domain.model.Reminder
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
