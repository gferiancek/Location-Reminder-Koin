package com.udacity.project4.presentation.ui.reminders.reminders_list

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.udacity.project4.domain.model.DataState
import com.udacity.project4.domain.model.Reminder
import com.udacity.project4.presentation.ui.reminders.reminders_list.RemindersListEvent.GetAllReminders
import com.udacity.project4.use_cases.reminders_list.GetAllRemindersUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RemindersListViewModel(
    private val getAllRemindersUseCase: GetAllRemindersUseCase
) : ViewModel() {

    private val _navigateToEditScreen = MutableLiveData<Reminder?>()
    val navigateToEditScreen: MutableLiveData<Reminder?>
        get() = _navigateToEditScreen

    // Not really used, because I was originally showing a snackbar from the
    // edit screen when adding/editing screens.  However, that required attaching
    // the snackbar to the Activity, which made the Instrumented Tests fail (since it
    // uses it's own activity instead of our MainActivity).  Upon doing this app again,
    // I would instead show that type of snackbar here.
    private val _snackbarMessage = MutableLiveData<String>()
    val snackbarMessage: MutableLiveData<String>
        get() = _snackbarMessage

    val remindersList = MutableLiveData<List<Reminder>>(listOf())

    val loading = MutableLiveData(false)

    init {
        onTriggerEvent(GetAllReminders)
    }

    private fun onTriggerEvent(event: RemindersListEvent) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                when (event) {
                    is GetAllReminders -> getAllReminders()
                }
            }
        }
    }

    private fun getAllReminders() {
        getAllRemindersUseCase.execute().onEach { dataState ->
            when (dataState) {
                is DataState.Loading -> loading.value = true
                is DataState.Data -> {
                    dataState.data?.let { list ->
                        remindersList.value = list
                        loading.value = false
                    }
                }
                is DataState.Error -> {
                    _snackbarMessage.value = dataState.message
                    loading.value = false
                }
            }
        }.launchIn(viewModelScope)
    }

    fun onNavigateToEditScreen(currentReminder: Reminder) {
        _navigateToEditScreen.value = currentReminder
    }

    fun onNavigateToEditScreenFinished() {
        _navigateToEditScreen.value = null
    }
}