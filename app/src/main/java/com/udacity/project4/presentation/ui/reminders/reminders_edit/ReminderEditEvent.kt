package com.udacity.project4.presentation.ui.reminders.reminders_edit

sealed class ReminderEditEvent {

    object AddNewReminderEvent : ReminderEditEvent()

    object EditCurrentReminderEvent : ReminderEditEvent()
}
