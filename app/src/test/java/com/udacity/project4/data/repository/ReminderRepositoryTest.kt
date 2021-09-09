package com.udacity.project4.data.repository

import com.google.common.truth.Truth.assertThat
import com.udacity.project4.data.cache.ReminderDaoFake
import com.udacity.project4.data.cache.ReminderDatabaseFake
import com.udacity.project4.data.cache.model.ReminderEntity
import com.udacity.project4.domain.model.Reminder
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class ReminderRepositoryTest {

    private lateinit var repository: ReminderRepositoryImpl

    @Before
    fun createRepository() {
        repository = ReminderRepositoryImpl(ReminderDaoFake(ReminderDatabaseFake()))
    }

    @Test
    fun `After initialization ReminderList should be empty`() = runBlockingTest {

        // Given an empty database, When getting list of reminders
        val flowList = repository.reminderList.toList()
        val reminderList = flowList[0]

        // Then returned list should be empty
        assertThat(reminderList).isEmpty()
    }

    @Test
    fun `After inserting into database ReminderList should contain Reminder object`() =
        runBlockingTest {

            // Given an empty database
            val flowList = repository.reminderList.toList()
            val reminderList = flowList[0]
            assertThat(reminderList).isEmpty()

            // When inserting a reminder object
            val reminder = Reminder()
            repository.insertReminder(reminder)

            // Then database should not be empty and contain reminder object
            assertThat(reminderList).isNotEmpty()
            assertThat(reminderList[0]).isInstanceOf(ReminderEntity::class.java)
            assertThat(reminder.id).isEqualTo(reminderList[0].id)
        }

    @Test
    fun `When given reminder id, Repository should return Reminder object with given id`() =
        runBlockingTest {

            // Given an id for a Reminder with title of "Call Mom" and location_name of "Home"
            // and a database with 3 Reminders, including the previously mentioned one.
            val titleString = "Call Mom"
            val locationString = "Home"
            val reminder = Reminder(title = titleString, location_name = locationString)
            val id = reminder.id
            repository.insertReminder(Reminder())
            repository.insertReminder(Reminder())
            repository.insertReminder(reminder)

            // When getting the reminder by its id
            val reminderToTest = repository.getReminder(id)

            // Then reminderToTest should have id, title, and location matching the variables defined above.
            assertThat(reminderToTest?.id).isEqualTo(id)
            assertThat(reminderToTest?.title).isEqualTo(titleString)
            assertThat(reminderToTest?.location_name).isEqualTo(locationString)
        }

    @Test
    fun `After updating item, ReminderList should have new Reminder at the updated index`() =
        runBlockingTest {

            // Given a database with 3 items, where index 1 has an id of idToUpdate and a title of ""
            repository.insertReminder(Reminder())
            val idToUpdate = Reminder().id
            repository.insertReminder(Reminder(id = idToUpdate))
            repository.insertReminder(Reminder())
            val flowItems = repository.reminderList.toList()
            val reminderList = flowItems[0]
            assertThat(reminderList[1].id).isEqualTo(idToUpdate)
            assertThat(reminderList[1].title).isEqualTo("")

            // When updating the item at index 1
            val titleString = "Title"
            val updatedReminder = Reminder(id = idToUpdate, title = titleString)
            repository.updateReminder(updatedReminder)

            // Then the reminder at index 1 should have an id of idToUpdate and title of titleString
            assertThat(reminderList[1].id).isEqualTo(idToUpdate)
            assertThat(reminderList[1].title).isEqualTo(titleString)
        }


    @Test
    fun `When given reminder id, Repository should delete Reminder object with matching id`() =
        runBlockingTest {

            // Given a database with 3 items, where index 0 has an id of idToDelete
            val reminder = Reminder()
            val idToDelete = reminder.id
            repository.insertReminder(reminder)
            repository.insertReminder(Reminder())
            repository.insertReminder(Reminder())
            val flowItems = repository.reminderList.toList()
            val reminderList = flowItems[0]
            assertThat(reminderList[0].id).isEqualTo(idToDelete)

            // When deleting the Reminder at index 0
            repository.deleteReminder(idToDelete)

            // Then Reminder with id of idToDelete should not be in the database
            val reminderToTest = repository.getReminder(idToDelete)
            assertThat(reminderToTest).isEqualTo(null)
        }
}