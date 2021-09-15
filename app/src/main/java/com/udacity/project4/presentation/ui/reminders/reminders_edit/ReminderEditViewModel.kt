package com.udacity.project4.presentation.ui.reminders.reminders_edit

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.udacity.project4.domain.model.DataState
import com.udacity.project4.domain.model.Reminder
import com.udacity.project4.presentation.ui.reminders.reminders_edit.ReminderEditEvent.AddNewReminderEvent
import com.udacity.project4.presentation.ui.reminders.reminders_edit.ReminderEditEvent.EditCurrentReminderEvent
import com.udacity.project4.use_cases.reminders_edit.AddReminderUseCase
import com.udacity.project4.use_cases.reminders_edit.EditReminderUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ReminderEditViewModel(
    private val addReminderUseCase: AddReminderUseCase,
    private val editReminderUseCase: EditReminderUseCase,
) : ViewModel() {

    var currentReminder = Reminder()
    var isEditing = false

    private val _snackbarMessage = MutableLiveData("")
    val snackbarMessage: MutableLiveData<String>
        get() = _snackbarMessage

    private val _eventSuccess = MutableLiveData<Boolean>()
    val eventSuccess: MutableLiveData<Boolean>
        get() = _eventSuccess


    fun onTriggerEvent(event: ReminderEditEvent) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                when (event) {
                    is AddNewReminderEvent -> addReminder()
                    is EditCurrentReminderEvent -> editReminder()
                }
            }
        }
    }

    private fun addReminder() {
        addReminderUseCase.execute(currentReminder).onEach { dataState ->
            when (dataState) {
                is DataState.Data -> {
                    dataState.data?.let {
                        _eventSuccess.value = true
                    }
                }
                is DataState.Error -> {
                    _snackbarMessage.value = dataState.message
                    _eventSuccess.value = false
                }
                is DataState.Loading -> return@onEach
            }
        }.launchIn(viewModelScope)
    }

    private fun editReminder() {
        editReminderUseCase.execute(currentReminder).onEach { dataState ->
            when (dataState) {
                is DataState.Data -> {
                    dataState.data?.let {
                        _eventSuccess.value = true
                    }
                }
                is DataState.Error -> {
                    _snackbarMessage.value = dataState.message
                    _eventSuccess.value = false
                }
                is DataState.Loading -> return@onEach
            }
        }.launchIn(viewModelScope)
    }

    fun onEventSuccessHandled() {
        _eventSuccess.value = false
    }

    fun onDisplayNewSnackbar(message: String) {
        _snackbarMessage.value = message
    }

    fun onSnackbarMessageDisplayed() {
        _snackbarMessage.value = ""
    }
}