package com.udacity.project4.data.cache

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import com.udacity.project4.MainCoroutineRule
import com.udacity.project4.data.cache.database.ReminderDao
import com.udacity.project4.data.cache.model.toReminder
import com.udacity.project4.data.cache.model.toReminderList
import com.udacity.project4.domain.model.Reminder
import com.udacity.project4.domain.model.toReminderEntity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@ExperimentalCoroutinesApi
@SmallTest
class ReminderDaoTest {

    @get: Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get: Rule
    var testScope = MainCoroutineRule()

    @Inject
    lateinit var dao: ReminderDao

    private lateinit var reminder: Reminder
    private lateinit var flowList: MutableList<List<Reminder>>

    @Before
    fun setup() {
        reminder = Reminder(
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
        flowList = mutableListOf()
    }

    @Test
    fun insertReminder_DatabaseShouldNotBeEmpty() = runBlockingTest {
        // Given an empty database
        flowList.add(dao.getAllReminders().first().toReminderList())
        assertThat(flowList[0]).isEmpty()

        // When adding a reminder to the database
        dao.insertReminder(reminder.toReminderEntity())

        // Then the database shouldn't be empty, and the returned List should contain a
        // Reminder object that is equal to the one we inserted
        flowList.add(dao.getAllReminders().first().toReminderList())
        assertThat(flowList[1]).isNotEmpty()
        assertThat(flowList[1][0]).isInstanceOf(Reminder::class.java)
        assertThat(flowList[1][0]).isEqualTo(reminder)
    }

    @Test
    fun updateReminder_DatabaseShouldReturnUpdatedReminder() = runBlockingTest {
        // Given a database with a reminder object in it
        dao.insertReminder(reminder.toReminderEntity())
        flowList.add(dao.getAllReminders().first().toReminderList())
        assertThat(flowList[0]).isNotEmpty()

        // When changing a field in the reminder and updating it in the database
        reminder.title = "New title"
        val rowsUpdated = dao.updateReminder(reminder.toReminderEntity())

        // Then rowsUpdated should equal 1, and getReminder should return a reminder with the
        // updated information
        assertThat(rowsUpdated).isEqualTo(1)
        assertThat(dao.getReminder(reminder.id)?.title).isEqualTo("New title")
    }

    @Test
    fun updateReminder_ReminderNotPresent_ShouldReturnZero() = runBlockingTest {
        // Given an empty database
        flowList.add(dao.getAllReminders().first().toReminderList())
        assertThat(flowList[0]).isEmpty()

        // When calling the Update method with a reminder
        val rowsUpdated = dao.updateReminder(reminder.toReminderEntity())

        // Then rowsUpdated should be zero
        assertThat(rowsUpdated).isEqualTo(0)
    }

    @Test
    fun getReminder_ShouldReturnReminderIfPresent() = runBlockingTest {
        // Given a non empty database
        dao.insertReminder(Reminder().toReminderEntity())
        dao.insertReminder(reminder.toReminderEntity())
        dao.insertReminder(Reminder().toReminderEntity())
        flowList.add(dao.getAllReminders().first().toReminderList())
        assertThat(flowList[0]).isNotEmpty()

        // When calling getReminder
        val retrievedReminder = dao.getReminder(reminder.id)?.toReminder()

        // Then retrievedReminder should equal reminder
        assertThat(retrievedReminder).isEqualTo(reminder)
    }

    @Test
    fun getReminder_ShouldReturnNullIfNotPresent() = runBlockingTest {
        // Given a non empty database
        dao.insertReminder(Reminder().toReminderEntity())
        dao.insertReminder(Reminder().toReminderEntity())
        flowList.add(dao.getAllReminders().first().toReminderList())
        assertThat(flowList[0]).isNotEmpty()

        // When calling getReminder with an id not in the database
        val retrievedReminder = dao.getReminder(reminder.id)?.toReminder()

        // Then retrievedReminder should be null
        assertThat(retrievedReminder).isNull()
    }

    @Test
    fun deleteReminder_ReminderShouldNotBePresentInDatabase() = runBlockingTest {
        // Given a database with Reminder object
        dao.insertReminder(reminder.toReminderEntity())
        flowList.add(dao.getAllReminders().first().toReminderList())
        assertThat(flowList[0]).isNotEmpty()

        // When deleting the single item in the database
        dao.deleteReminder(reminder.id)

        // Then the database should be empty
        flowList.add(dao.getAllReminders().first().toReminderList())
        assertThat(flowList[1]).isEmpty()
    }

    @Test
    fun deleteAllReminders_DatabaseShouldBeEmpty() = runBlockingTest {
        // Given a database with multiple Reminder objects
        dao.insertReminder(Reminder().toReminderEntity())
        dao.insertReminder(Reminder().toReminderEntity())
        dao.insertReminder(Reminder().toReminderEntity())
        flowList.add(dao.getAllReminders().first().toReminderList())
        assertThat(flowList[0]).isNotEmpty()

        // When calling deleteAllReminders
        dao.deleteAllReminders()

        // Then the database should be empty
        flowList.add(dao.getAllReminders().first().toReminderList())
        assertThat(flowList[1]).isEmpty()
    }
}