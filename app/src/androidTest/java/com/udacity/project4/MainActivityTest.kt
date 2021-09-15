package com.udacity.project4

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.udacity.project4.di.dataModuleFake
import com.udacity.project4.domain.model.Reminder
import com.udacity.project4.presentation.MainActivity
import com.udacity.project4.util.DataBindingIdlingResource
import com.udacity.project4.util.EspressoIdlingResource
import com.udacity.project4.util.monitorActivity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.Matchers
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules
import org.koin.test.KoinTest

@LargeTest
@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class MainActivityTest : KoinTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private val dataBindingIdlingResource = DataBindingIdlingResource()

    @Before
    fun setup() {
        loadKoinModules(dataModuleFake)
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
        IdlingRegistry.getInstance().register(dataBindingIdlingResource)
    }

    @After
    fun tearDown() {
        unloadKoinModules(dataModuleFake)
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
        IdlingRegistry.getInstance().unregister(dataBindingIdlingResource)
    }

    @Test
    fun addNewTask() = runBlockingTest {

        val scenario = ActivityScenario.launch(MainActivity::class.java)
        dataBindingIdlingResource.monitorActivity(scenario)

        onView(withId(R.id.fab_reminders_add)).perform(click())
        onView(withId(R.id.action_bar)).check(matches(hasDescendant(withText("Add Reminder"))))

        onView(withId(R.id.et_reminder_title)).perform(typeText("Title"))
        onView(withId(R.id.et_reminder_description)).perform(
            typeText("Description"),
            closeSoftKeyboard()
        )
        onView(withId(R.id.mv_reminder_location_map)).perform(longClick())
        onView(withId(R.id.et_sheet_name)).perform(
            typeText("Location"),
            closeSoftKeyboard()
        )
        onView(withId(R.id.tv_geofence_request_header)).perform(click())
        onView(withId(R.id.spinner_transition)).perform(click())
        onData(Matchers.`is`("Enter")).perform(click())
        onView(withId(R.id.fab_save)).perform(click())

        onView(withId(R.id.tv_list_item_title)).check(matches(withText("Title")))
        onView(withId(R.id.tv_list_item_description)).check(matches(withText("Description")))
        onView(withId(R.id.tv_list_item_location)).check(matches(withText("Location")))

        scenario.close()
    }

    /**
     * Make sure ReminderEditFragment's GeofenceRequest.setInitialTrigger is set to 0, otherwise instant notification
     * will block UI elements necessary for the test.
     */
    @Test
    fun editTask() = runBlockingTest {
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

        val scenario = ActivityScenario.launch(MainActivity::class.java)
        dataBindingIdlingResource.monitorActivity(scenario)

        onView(withId(R.id.fab_reminders_add)).perform(click())
        onView(withId(R.id.action_bar)).check(matches(hasDescendant(withText("Add Reminder"))))

        onView(withId(R.id.et_reminder_title)).perform(typeText(reminder.title))
        onView(withId(R.id.et_reminder_description)).perform(
            typeText(reminder.description),
            closeSoftKeyboard()
        )
        onView(withId(R.id.mv_reminder_location_map)).perform(longClick())
        onView(withId(R.id.et_sheet_name)).perform(
            typeText(reminder.location_name),
            closeSoftKeyboard()
        )
        onView(withId(R.id.tv_geofence_request_header)).perform(click())
        onView(withId(R.id.spinner_transition)).perform(click())
        onData(Matchers.`is`("Enter")).perform(click())
        onView(withId(R.id.fab_save)).perform(click())

        onView(withText(reminder.title)).perform(click())
        onView(withId(R.id.action_bar)).check(matches(hasDescendant(withText("Edit Reminder"))))
        onView(withId(R.id.et_reminder_title)).check(matches(withText(reminder.title)))
        onView(withId(R.id.et_reminder_title)).perform(
            typeText(" - Edited"),
            closeSoftKeyboard()
        )
        onView(withId(R.id.et_reminder_description)).check(matches(withText(reminder.description)))

        onView(withId(R.id.mv_reminder_location_map)).perform(longClick())
        onView(withId(R.id.tv_geofence_request_header)).perform(click())
        onView(withId(R.id.et_sheet_name)).perform(typeText("No Idea"))

        onView(withId(R.id.spinner_expiration)).check(matches(hasDescendant(withText("Never"))))
        onView(withId(R.id.spinner_transition)).check(matches(hasDescendant(withText("Enter"))))

        onView(withId(R.id.fab_save)).perform(click())

        onView(withId(R.id.tv_list_item_title)).check(matches(withText(reminder.title + " - Edited")))
    }
}