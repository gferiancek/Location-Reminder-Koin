package com.udacity.project4.presentation.ui.reminders.reminders_edit

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.udacity.project4.data.cache.ReminderDaoFake
import com.udacity.project4.data.cache.ReminderDatabaseFake
import com.udacity.project4.data.repository.ReminderRepositoryFake
import com.udacity.project4.presentation.ui.getOrAwaitValue
import com.udacity.project4.use_cases.reminders_edit.AddReminderUseCase
import com.udacity.project4.use_cases.reminders_edit.EditReminderUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class ReminderEditViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: ReminderEditViewModel

    @Before
    fun createViewModel() {
        val reminderDao = ReminderDaoFake(ReminderDatabaseFake())
        val repository = ReminderRepositoryFake(reminderDao)
        val addReminderUseCase = AddReminderUseCase(repository)
        val editReminderUseCase = EditReminderUseCase(repository)
        viewModel = ReminderEditViewModel(addReminderUseCase, editReminderUseCase)
    }

    @Test
    fun `When onEventSuccessHandled is called Then eventSuccess is set to false`() {
        // Given eventSuccess that is true
        viewModel.eventSuccess.value = true
        assertThat(viewModel.eventSuccess.getOrAwaitValue()).isEqualTo(true)

        // When onEventSuccessHandled is called
        viewModel.onEventSuccessHandled()

        // Then eventSuccess should be false
        assertThat(viewModel.eventSuccess.getOrAwaitValue()).isEqualTo(false)
    }

    @Test
    fun `When onDisplayNewSnackbar is called Then snackbarMessage should not be blank`() {
        // Given snackbarMessage with a value of ""
        assertThat(viewModel.snackbarMessage.value).isEmpty()

        // When onDisplayNewSnackbar is called
        viewModel.onDisplayNewSnackbar("message")

        // Then snackbarMessage should not be blank
        assertThat(viewModel.snackbarMessage.getOrAwaitValue()).isNotEmpty()
    }

    @Test
    fun `When onSnackbarMessageDisplayed is called Then snackbarMessage should be blank`() {
        // Given snackbarMessage that isn't blank
        viewModel.onDisplayNewSnackbar("message")
        assertThat(viewModel.snackbarMessage.getOrAwaitValue()).isNotEmpty()

        // When onSnackbarMessageDisplayed is called
        viewModel.onSnackbarMessageDisplayed()

        // Then snackbarMessage should be blank
        assertThat(viewModel.snackbarMessage.getOrAwaitValue()).isEmpty()
    }
}