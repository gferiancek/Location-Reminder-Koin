package com.udacity.project4.presentation.ui.reminders.detail

import android.os.Bundle
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.udacity.project4.R
import com.udacity.project4.domain.model.Reminder
import com.udacity.project4.util.DataBindingIdlingResource
import com.udacity.project4.util.monitorFragment
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@MediumTest
class ReminderDetailFragmentTest {

    private val dataBindingIdlingResource = DataBindingIdlingResource()

    @Before
    fun setup() {
        IdlingRegistry.getInstance().register(dataBindingIdlingResource)
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(dataBindingIdlingResource)
    }

    @Test
    fun bundledReminderDetails_DisplayedInUi() {
        // Given Reminder object
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

        // When ReminderDetailFragment is launched with above Reminder
        val bundle = Bundle().apply {
            putParcelable("currentReminder", reminder)
            putString("label", reminder.location_name)
        }

        val scenario = launchFragmentInContainer<ReminderDetailFragment>(
            bundle,
            R.style.Theme_LocationReminder
        )
        dataBindingIdlingResource.monitorFragment(scenario)

        // Then verify the UI matches the above details
        onView(withId(R.id.tv_detail_title))
            .check(matches(isDisplayed()))
            .check(matches(withText(reminder.title)))
        onView(withId(R.id.mv_detail_map))
            .check(matches(isDisplayed()))
        onView(withId(R.id.tv_detail_description))
            .check(matches(isDisplayed()))
            .check(matches(withText(reminder.description)))
        onView(withId(R.id.tv_detail_expiration))
            .check(matches(isDisplayed()))
            .check(matches(withText("Never Expires")))
        onView(withId(R.id.tv_detail_transition))
            .check(matches(isDisplayed()))
            .check(matches(withText("Transition Type: Exit")))
    }
}