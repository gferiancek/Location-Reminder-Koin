package com.udacity.project4.domain.use_cases.reminders_list

import com.udacity.project4.data.cache.model.toReminderList
import com.udacity.project4.data.repository.ReminderRepository
import com.udacity.project4.domain.model.DataState
import com.udacity.project4.domain.model.Reminder
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow

class GetAllRemindersUseCase(
    private val repository: ReminderRepository
) {
    fun execute(): Flow<DataState<List<Reminder>>> = flow {
        try {
            emit(DataState.loading())
            repository.reminderList.collect { reminders ->
                emit(DataState.data(reminders.toReminderList()))
            }
        } catch (e: Exception) {
            emit(DataState.error(message = e.message.toString()))
        }
    }
}