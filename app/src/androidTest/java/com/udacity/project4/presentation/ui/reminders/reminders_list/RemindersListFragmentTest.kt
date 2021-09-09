package com.udacity.project4.presentation.ui.reminders.reminders_list

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.MediumTest
import com.udacity.project4.MainCoroutineRule
import com.udacity.project4.R
import com.udacity.project4.data.repository.ReminderRepositoryImpl
import com.udacity.project4.domain.model.Reminder
import com.udacity.project4.launchFragmentInHiltContainer
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltAndroidTest
@MediumTest
class RemindersListFragmentTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var testScope = MainCoroutineRule()

    @Inject
    lateinit var repository: ReminderRepositoryImpl

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun clickingFab_navigatesToReminderEditFragment() {
        // Given an instance of the RemindersListFragment
        val navController = mock(NavController::class.java)
        launchFragmentInHiltContainer<RemindersListFragment> {
            Navigation.setViewNavController(requireView(), navController)
        }
        // When clicking on the + FAB
        onView(withId(R.id.fab_reminders_add))
            .perform(click())

        // Then we should navigate to the EditReminderFragment with a label of "Add Reminder" passed
        verify(navController).navigate(
            RemindersListFragmentDirections.actionRemindersListFragmentToEditReminderFragment(
                "Add Reminder"
            )
        )
    }

    @Test
    fun clickingReminder_navigatesToReminderEditFragment() = testScope.dispatcher.runBlockingTest {
        // Given an instance of the RemindersListFragment
        val navController = mock(NavController::class.java)
        launchFragmentInHiltContainer<RemindersListFragment> {
            Navigation.setViewNavController(requireView(), navController)
        }

        // When it has a Reminder in the local database
        val reminder = Reminder(
            title = "Study Hard",
            description = "Submit Project and pass, then tackle Capstone",
            latitude = 32.0102,
            longitude = -12.3013,
            location_name = "No Idea",
            geofence_radius = 350f,
            _expiration_duration = 1f,
            _expiration_interval = 0,
            transition_type = 1
        )
        repository.insertReminder(reminder)

        // Then clicking on that Reminder should navigate to the EditReminderFragment with that reminder
        // and a label of "Edit Reminder" attached.
        onView(withId(R.id.rv_reminders))
            .perform(
                RecyclerViewActions.actionOnItem<RecyclerView.ViewHolder>(
                    hasDescendant(withText(reminder.title)), click()
                )
            )
        val action =
            RemindersListFragmentDirections.actionRemindersListFragmentToEditReminderFragment("Edit Reminder")
        action.currentReminder = reminder
        verify(navController).navigate(action)
    }
}