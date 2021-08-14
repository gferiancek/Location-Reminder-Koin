package com.example.locationreminder.presentation.ui.reminders

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.locationreminder.domain.Reminder
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RemindersViewModel @Inject constructor() : ViewModel() {

    private val _navigateToEditScreen = MutableLiveData<Boolean>()
    val navigateToEditScreen: MutableLiveData<Boolean>
        get() = _navigateToEditScreen

    val remindersList = MutableLiveData<MutableList<Reminder>>(mutableListOf())
    lateinit var currentReminder: Reminder

    var idCount = 0
    fun onNavigateToEditScreen(reminder: Reminder) {
        currentReminder = reminder
        _navigateToEditScreen.value = true
    }

    fun onNavigateToEditScreenFinished() {
        _navigateToEditScreen.value = false
    }

    fun addReminder() {
        remindersList.value?.let {
            if (currentReminder.id + 1 > it.size) {
                val newList = remindersList.value
                newList?.add(currentReminder)
                remindersList.value = newList!!
            } else {
                remindersList.value!!.set(currentReminder.id, currentReminder)
            }
        }

    }
}