package com.example.locationreminder.presentation.ui.reminders.reminders_list

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.locationreminder.domain.Reminder
import com.example.locationreminder.presentation.ui.reminders.reminders_list.RemindersListEvent.GetAllReminders
import com.example.locationreminder.use_cases.reminders_list.GetAllRemindersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class RemindersListViewModel @Inject constructor(
    private val getAllRemindersUseCase: GetAllRemindersUseCase
) : ViewModel() {

    private val _navigateToEditScreen = MutableLiveData<Reminder?>()
    val navigateToEditScreen: MutableLiveData<Reminder?>
        get() = _navigateToEditScreen

    private val _snackbarMessage = MutableLiveData<String>()
    val snackbarData: MutableLiveData<String>
        get() = _snackbarMessage

    val remindersList = MutableLiveData<List<Reminder>>(listOf())

    val loading = MutableLiveData(false)

    init {
        onTriggerEvent(GetAllReminders)
    }

    fun onTriggerEvent(event: RemindersListEvent) {
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
            dataState.loading.let {
                loading.value = true
            }
            dataState.data?.let {
                remindersList.value = it
                loading.value = false
            }
            dataState.error?.let {
                _snackbarMessage.value = it
                loading.value = false
            }
        }.launchIn(viewModelScope)
    }

    fun onNavigateToEditScreen(reminder: Reminder) {
        _navigateToEditScreen.value = reminder
    }

    fun onNavigateToEditScreenFinished() {
        _navigateToEditScreen.value = null
    }
}