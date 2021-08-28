package com.example.locationreminder.presentation.ui.reminders.reminders_edit

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.locationreminder.domain.model.Reminder
import com.example.locationreminder.domain.use_cases.reminders_edit.AddReminderUseCase
import com.example.locationreminder.domain.use_cases.reminders_edit.EditReminderUseCase
import com.example.locationreminder.domain.use_cases.reminders_edit.RetrieveReminderUseCase
import com.example.locationreminder.presentation.ui.reminders.reminders_edit.ReminderEditEvent.*
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
    private val retrieveReminderUseCase: RetrieveReminderUseCase
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
                    is RetrieveReminderEvent -> retrieveReminder(id = event.id)
                    is DeleteCurrentReminderEvent -> deleteReminder(id = event.id)
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

    private fun retrieveReminder(id: String) {
        retrieveReminderUseCase.execute(id).onEach { dataState ->
            dataState.data?.let { data ->
                currentReminder = data
            }
            dataState.error?.let { message ->
                _snackbarMessage.value = message
            }
        }.launchIn(viewModelScope)
    }

    private fun deleteReminder(id: Long) {
        // execute DeleteReminderUseCase
    }

    fun onEventSuccessHandled() {
        _eventSuccess.value = false
    }

    fun onSnackbarMessageDisplayed() {
        _snackbarMessage.value = ""
    }
}