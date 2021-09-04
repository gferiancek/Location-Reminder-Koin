package com.udacity.project4.domain.use_cases.reminders_edit

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
            emit(DataState.loading())
            when {
                reminder.latitude == 0.0 && reminder.longitude == 0.0 -> {
                    emit(DataState.error(message = "Please select a location on the map for your geofence"))
                }
                reminder.transition_type == 3 -> {
                    emit(DataState.error(message = "Please select a transition type"))
                }
                reminder.hasBlankTextFields() -> {
                    emit(DataState.error(message = "Please fill out all text fields before submitting reminder"))
                }
                else -> {
                    repository.insertReminder(reminder)
                    emit(DataState.data(repository.getReminder(reminder.id)))
                }
            }
        } catch (e: Exception) {
            emit(DataState.error(message = e.message.toString()))
        }
    }
}