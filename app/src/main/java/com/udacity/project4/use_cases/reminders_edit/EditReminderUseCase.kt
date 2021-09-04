package com.udacity.project4.use_cases.reminders_edit

import com.udacity.project4.data.repository.ReminderRepository
import com.udacity.project4.domain.model.DataState
import com.udacity.project4.domain.model.Reminder
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class EditReminderUseCase(
    private val repository: ReminderRepository
) {
    fun execute(
        reminder: Reminder
    ): Flow<DataState<Reminder>> = flow {
        try {
            emit(DataState.Loading())
            when {
                reminder.hasBlankTextFields() -> {
                    emit(DataState.Error("Please fill out all text fields before submitting reminder"))
                }
                else -> {
                    repository.updateReminder(reminder)
                    emit(DataState.Data(reminder))
                }
            }
        } catch (e: Exception) {
            emit(DataState.Error(e.message.toString()))
        }
    }
}
