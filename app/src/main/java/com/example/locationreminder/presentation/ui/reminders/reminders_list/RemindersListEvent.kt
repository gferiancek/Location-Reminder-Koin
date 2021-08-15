package com.example.locationreminder.presentation.ui.reminders.reminders_list

sealed class RemindersListEvent {

    object GetAllReminders : RemindersListEvent()
}