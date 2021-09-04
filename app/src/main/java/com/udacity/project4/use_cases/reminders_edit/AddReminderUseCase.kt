package com.udacity.project4.use_cases.reminders_edit

import com.udacity.project4.data.repository.ReminderRepository
import com.udacity.project4.domain.model.DataState
import com.udacity.project4.domain.model.Reminder
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class AddReminderUseCase(
    private val repository: ReminderRepository
) {
    fun execute(
        reminder: Reminder
    ): Flow<DataState<Reminder>> = flow {
        try {
            emit(DataState.Loading())
            when {
                reminder.latitude == 0.0 && reminder.longitude == 0.0 -> {
                    emit(DataState.Error(message = "Please select a location on the map for your geofence"))
                }
                reminder.transition_type == 3 -> {
                    emit(DataState.Error(message = "Please select a transition type"))
                }
                reminder.hasBlankTextFields() -> {
                    emit(DataState.Error(message = "Please fill out all text fields before submitting reminder"))
                }
                else -> {
                    repository.insertReminder(reminder)
                    val cachedReminder = repository.getReminder(reminder.id)
                    if (cachedReminder != null) {
                        emit(DataState.Data(cachedReminder))
                    }
                }
            }
        } catch (e: Exception) {
            emit(DataState.Error(message = e.message.toString()))
        }
    }
}