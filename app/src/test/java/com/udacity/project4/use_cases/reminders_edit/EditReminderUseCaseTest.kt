package com.udacity.project4.use_cases.reminders_edit

import com.google.common.truth.Truth.assertThat
import com.udacity.project4.data.cache.database.ReminderDaoFake
import com.udacity.project4.data.cache.database.ReminderDatabaseFake
import com.udacity.project4.data.repository.ReminderRepositoryFake
import com.udacity.project4.domain.model.DataState
import com.udacity.project4.domain.model.Reminder
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class EditReminderUseCaseTest {

    private lateinit var editReminderUseCase: EditReminderUseCase

    @Before
    fun createRepository() {
        val reminderDao = ReminderDaoFake(ReminderDatabaseFake())
        val repository = ReminderRepositoryFake(reminderDao)
        editReminderUseCase = EditReminderUseCase(repository)
    }

    /**
     * Note: Once a user has selected latitude, longitude, and transition type there is no way to
     * unselect them. Therefore, we don't need to test those scenarios and only need to worry about
     * them deleting the text in the EditTexts.
     */
    @Test
    fun `When Reminder hasBlankTextFields() is true then UseCase will emit error`() =
        runBlockingTest {
            // Given a reminder with Lat & Lng not 0, transition_type not 3, but has a blank text field (here, location_name is blank)
            val reminder = Reminder(
                latitude = 12.03,
                longitude = 31.0120,
                transition_type = 1,
                title = "title",
                description = "description"
            )

            // When adding that reminder with AddReminderUseCase
            val flowList = editReminderUseCase.execute(reminder).toList()

            // Then first emission should be of DataState.Loading
            // and second emission should be of DataState.Error
            assertThat(flowList[0] is DataState.Loading)
            assertThat(flowList[1] is DataState.Error)
        }

    /**
     * Note: Since we have a repository test already, I did not verify that the cache was empty
     * before updating the reminder and check that it was no longer empty afterwards.  It cannot pass
     * in the ReminderRepositoryTest and fail here, so the testing would be redundant.
     */
    @Test
    fun `When Reminder has required data then UseCase will emit reminder`() = runBlockingTest {
        // Given a reminder with required data and an empty cache
        val reminder = Reminder(
            latitude = 12.03,
            longitude = 31.0120,
            transition_type = 1,
            title = "title",
            description = "description",
            location_name = "location name"
        )

        // When adding that reminder with AddReminderUseCase and then retrieving it from the cache
        val flowList = editReminderUseCase.execute(reminder).toList()

        // Then the first emission should be of DataState.Loading
        // the second emission should be of DataState.Data
        // and the data should be equal to the above reminder
        assertThat(flowList[0] is DataState.Loading)
        assertThat(flowList[1] is DataState.Data)
        assertThat((flowList[1] as DataState.Data).data).isEqualTo(reminder)
    }
}