package com.udacity.project4.domain.use_cases.reminders_list

import com.udacity.project4.cache.database.ReminderDao
import com.udacity.project4.cache.model.toReminderList
import com.udacity.project4.domain.model.DataState
import com.udacity.project4.domain.model.Reminder
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