package com.example.locationreminder.domain.use_cases.reminders_edit

import com.example.locationreminder.cache.database.ReminderDao
import com.example.locationreminder.cache.model.toReminder
import com.example.locationreminder.domain.DataState
import com.example.locationreminder.domain.Reminder
import com.example.locationreminder.domain.toReminderEntity
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class AddReminderUseCase(
    private val reminderDao: ReminderDao
) {
    fun execute(
        reminder: Reminder
    ): Flow<DataState<Reminder>> = flow {
        try {
            emit(DataState.loading())
            when {
                reminder.latLng == LatLng(0.0, 0.0) -> {
                    emit(DataState.error<Reminder>(message = "Please select a location on the map for your geofence"))
                }
                reminder.transitionType == 3 -> {
                    emit(DataState.error<Reminder>(message = "Please select a transition type"))
                }
                reminder.missingData() -> {
                    emit(DataState.error<Reminder>(message = "Please fill out all fields before submitting reminder"))
                }
                else -> {
                    val cachedReminder = reminderDao.insertReminder(reminder.toReminderEntity())
                    emit(DataState.data(reminderDao.getReminder(cachedReminder).toReminder()))
                }
            }
        } catch (e: Exception) {
            emit(DataState.error<Reminder>(message = e.message.toString()))
        }
    }
}