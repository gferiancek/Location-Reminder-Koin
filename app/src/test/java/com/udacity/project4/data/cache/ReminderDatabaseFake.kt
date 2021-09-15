package com.udacity.project4.data.cache

import com.udacity.project4.data.cache.model.ReminderEntity


class ReminderDatabaseFake {
    val reminders = mutableListOf<ReminderEntity>()
}