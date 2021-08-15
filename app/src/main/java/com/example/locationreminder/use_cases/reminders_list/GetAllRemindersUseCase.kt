package com.example.locationreminder.use_cases.reminders_list

import com.example.locationreminder.cache.database.ReminderDao
import com.example.locationreminder.cache.model.toReminderList
import com.example.locationreminder.domain.DataState
import com.example.locationreminder.domain.Reminder
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow

class GetAllRemindersUseCase(
    private val remindersDao: ReminderDao
) {
    fun execute(): Flow<DataState<List<Reminder>>> = flow {
        try {
            emit(DataState.loading())
            remindersDao.getAllReminders().collect { reminders ->
                emit(DataState.data(reminders.toReminderList()))
            }
        } catch (e: Exception) {
            emit(DataState.error<List<Reminder>>(message = e.message.toString()))
        }
    }
}