package com.udacity.project4.presentation.ui.reminders.reminders_list

sealed class RemindersListEvent {

    object GetAllReminders : RemindersListEvent()
}