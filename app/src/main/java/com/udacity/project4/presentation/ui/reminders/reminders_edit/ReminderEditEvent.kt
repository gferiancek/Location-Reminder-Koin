package com.udacity.project4.presentation.ui.reminders.reminders_edit

sealed class ReminderEditEvent {

    object AddNewReminderEvent : ReminderEditEvent()

    object EditCurrentReminderEvent : ReminderEditEvent()

    data class RetrieveReminderEvent(val id: String) : ReminderEditEvent()

    data class DeleteCurrentReminderEvent(val id: Long) : ReminderEditEvent()
}
