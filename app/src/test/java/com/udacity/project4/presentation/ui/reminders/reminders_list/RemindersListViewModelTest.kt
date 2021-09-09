package com.udacity.project4.presentation.ui.reminders.reminders_list

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.udacity.project4.data.cache.ReminderDaoFake
import com.udacity.project4.data.cache.ReminderDatabaseFake
import com.udacity.project4.data.repository.ReminderRepositoryFake
import com.udacity.project4.domain.model.Reminder
import com.udacity.project4.presentation.ui.getOrAwaitValue
import com.udacity.project4.use_cases.reminders_list.GetAllRemindersUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class RemindersListViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: RemindersListViewModel

    @Before
    fun createViewModel() {
        val reminderDao = ReminderDaoFake(ReminderDatabaseFake())
        val repository = ReminderRepositoryFake(reminderDao)
        val getAllRemindersUseCase = GetAllRemindersUseCase(repository)
        viewModel = RemindersListViewModel(getAllRemindersUseCase)
    }

    @Test
    fun `When calling onNavigateToEditScreen Then navigateToEditScreen is not null`() {
        // Given a new Reminder and a null navigateToEditScreen
        val reminder = Reminder()
        assertThat(viewModel.navigateToEditScreen.value).isEqualTo(null)

        // When calling onNavigateToEditScreen
        viewModel.onNavigateToEditScreen(reminder)

        // Then navigateToEditScreen is not null
        assertThat(viewModel.navigateToEditScreen.getOrAwaitValue()).isNotEqualTo(null)
    }

    @Test
    fun `When finished navigating Then navigateToEditScreen is reset to null`() {
        // Given a navigation call making navigateToEditScreen not null
        viewModel.onNavigateToEditScreen(Reminder())
        assertThat(viewModel.navigateToEditScreen.getOrAwaitValue()).isNotEqualTo(null)

        // When calling onNavigateToEditScreenFinished
        viewModel.onNavigateToEditScreenFinished()

        // Then navigateToEditScreen should be null
        assertThat(viewModel.navigateToEditScreen.getOrAwaitValue()).isEqualTo(null)
    }
}