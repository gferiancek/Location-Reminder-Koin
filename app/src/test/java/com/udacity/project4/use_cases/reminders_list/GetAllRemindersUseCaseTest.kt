package com.udacity.project4.use_cases.reminders_list

import com.google.common.truth.Truth.assertThat
import com.udacity.project4.data.cache.ReminderDaoFake
import com.udacity.project4.data.cache.ReminderDatabaseFake
import com.udacity.project4.data.repository.ReminderRepositoryFake
import com.udacity.project4.domain.model.DataState
import com.udacity.project4.domain.model.Reminder
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test

@ExperimentalCoroutinesApi
class GetAllRemindersUseCaseTest {

    /**
     * The only thing that can go wrong in this UseCase is that an exception triggers the catch block.
     * Any thrown exception will print to the logs, so I don't think there is a reason to test it here.
     */
    @Test
    fun `When collecting from repository then UseCase will emit List of Reminders`() =
        runBlockingTest {
            // Given the repository, regardless of whether or not data is in the cache
            val reminderDao = ReminderDaoFake(ReminderDatabaseFake())
            val repository = ReminderRepositoryFake(reminderDao)
            val getAllRemindersUseCase = GetAllRemindersUseCase(repository)

            // When calling the UseCase
            val flowList = getAllRemindersUseCase.execute().toList()

            // Then the first emission should be of DataState.Loading
            // the second emission should be of DataState.Data
            // and data should be of type List<Reminder>
            assertThat(flowList[0] is DataState.Loading)
            assertThat(flowList[1] is DataState.Data)
            assertThat((flowList[1] as DataState.Data).data is List<Reminder>)
        }
}