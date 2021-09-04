package com.udacity.project4.presentation.ui.reminders.reminders_edit

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.udacity.project4.domain.model.Reminder
import com.udacity.project4.domain.use_cases.reminders_edit.AddReminderUseCase
import com.udacity.project4.domain.use_cases.reminders_edit.EditReminderUseCase
import com.udacity.project4.presentation.ui.reminders.reminders_edit.ReminderEditEvent.AddNewReminderEvent
import com.udacity.project4.presentation.ui.reminders.reminders_edit.ReminderEditEvent.EditCurrentReminderEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ReminderEditViewModel @Inject constructor(
    private val addReminderUseCase: AddReminderUseCase,
    private val editReminderUseCase: EditReminderUseCase,
) : ViewModel() {

    var currentReminder = Reminder()
    var isEditing = false

    private val _snackbarMessage = MutableLiveData<String>()
    val snackbarMessage: MutableLiveData<String>
        get() = _snackbarMessage

    private val _eventSuccess = MutableLiveData<Boolean>()
    val eventSuccess: MutableLiveData<Boolean>
        get() = _eventSuccess

    fun displayNewSnackbar(message: String) {
        _snackbarMessage.value = message
    }

    fun onTriggerEvent(event: ReminderEditEvent) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                when (event) {
                    is AddNewReminderEvent -> addReminder()
                    is EditCurrentReminderEvent -> editReminder()
                    else -> _snackbarMessage.value = "Unknown event. Please try again."
                }
            }
        }
    }

    private fun addReminder() {
        addReminderUseCase.execute(currentReminder).onEach { dataState ->
            dataState.data?.let { data ->
                _snackbarMessage.value = "Successfully added ${data.title} reminder"
                _eventSuccess.value = true
            }
            dataState.error?.let { message ->
                _snackbarMessage.value = message
                _eventSuccess.value = false
            }
        }.launchIn(viewModelScope)
    }

    private fun editReminder() {
        editReminderUseCase.execute(currentReminder).onEach { dataState ->
            dataState.data?.let { data ->
                _snackbarMessage.value = "Successfully updated the ${data.title} reminder"
                _eventSuccess.value = true
            }

            dataState.error?.let { message ->
                _snackbarMessage.value = message
                _eventSuccess.value = false
            }
        }.launchIn(viewModelScope)
    }

    fun onEventSuccessHandled() {
        _eventSuccess.value = false
    }

    fun onSnackbarMessageDisplayed() {
        _snackbarMessage.value = ""
    }
}