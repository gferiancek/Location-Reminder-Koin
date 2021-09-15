package com.udacity.project4.presentation.ui.reminders.reminders_edit

import android.os.Bundle
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.udacity.project4.R
import com.udacity.project4.di.dataModuleFake
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.Matchers.`is`
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@MediumTest
class ReminderEditFragmentTest {

    @Before
    fun setup() {
        loadKoinModules(dataModuleFake)
    }

    @After
    fun tearDown() {
        unloadKoinModules(dataModuleFake)
    }

    @Test
    fun clickingSave_NoLocation_ShowsSnackbar() {
        val navController = mock(NavController::class.java)

        val bundle = Bundle().apply {
            putString("label", "Add Reminder")
        }
        val scenario = launchFragmentInContainer<ReminderEditFragment>(
            fragmentArgs = bundle, R.style.Theme_LocationReminder
        )
        scenario.onFragment {
            Navigation.setViewNavController(it.view!!, navController)
        }

        onView(withId(R.id.fab_save)).perform(click())

        onView(withText("Please select a location on the map for your geofence"))
            .check(matches(isDisplayed()))
    }

    @Test
    fun clickingSave_NoTransitionType_ShowsSnackbar() {
        val navController = mock(NavController::class.java)

        val bundle = Bundle().apply {
            putString("label", "Add Reminder")
        }

        val scenario = launchFragmentInContainer<ReminderEditFragment>(
            fragmentArgs = bundle, R.style.Theme_LocationReminder
        )
        scenario.onFragment {
            Navigation.setViewNavController(it.view!!, navController)
        }

        onView(withId(R.id.mv_reminder_location_map)).perform(longClick())

        onView(withId(R.id.fab_save)).perform(click())

        onView(withText("Please select a transition type"))
            .check(matches(isDisplayed()))
    }

    @Test
    fun clickingSave_HasBlankTextFields_ShowsSnackbar() {
        val navController = mock(NavController::class.java)

        val bundle = Bundle().apply {
            putString("label", "Add Reminder")
        }
        val scenario = launchFragmentInContainer<ReminderEditFragment>(
            fragmentArgs = bundle, R.style.Theme_LocationReminder
        )
        scenario.onFragment {
            Navigation.setViewNavController(it.view!!, navController)
        }

        onView(withId(R.id.mv_reminder_location_map)).perform(longClick())
        onView(withId(R.id.tv_geofence_request_header)).perform(click())
        onView(withId(R.id.spinner_transition)).perform(click())
        onData(`is`("Enter")).perform(click())
        onView(withId(R.id.fab_save)).perform(click())

        onView(withText("Please fill out all text fields before submitting reminder"))
            .check(matches(isDisplayed()))
    }

    @Test
    fun clickingSave_AllDataPresent_NavigatesToListFragment() {
        val navController = mock(NavController::class.java)
        val bundle = Bundle().apply {
            putString("label", "Add Reminder")
        }

        val scenario = launchFragmentInContainer<ReminderEditFragment>(
            fragmentArgs = bundle, R.style.Theme_LocationReminder
        )
        scenario.onFragment {
            Navigation.setViewNavController(it.view!!, navController)
        }
        onView(withId(R.id.et_reminder_title)).perform(typeText("Title"))
        onView(withId(R.id.et_reminder_description)).perform(
            typeText("Description"),
            closeSoftKeyboard()
        )
        onView(withId(R.id.mv_reminder_location_map)).perform(longClick())
        onView(withId(R.id.et_sheet_name)).perform(typeText("Location"), closeSoftKeyboard())
        onView(withId(R.id.tv_geofence_request_header)).perform(click())
        onView(withId(R.id.spinner_transition)).perform(click())
        onData(`is`("Enter")).perform(click())
        onView(withId(R.id.fab_save)).perform(click())

        verify(navController).navigate(
            ReminderEditFragmentDirections.actionEditReminderFragmentToRemindersListFragment()
        )
    }
}
