package com.udacity.project4.di

import com.udacity.project4.cache.database.ReminderDao
import com.udacity.project4.domain.use_cases.reminders_edit.AddReminderUseCase
import com.udacity.project4.domain.use_cases.reminders_edit.EditReminderUseCase
import com.udacity.project4.domain.use_cases.reminders_edit.RetrieveReminderUseCase
import com.udacity.project4.domain.use_cases.reminders_list.GetAllRemindersUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object UseCasesModule {

    @ViewModelScoped
    @Provides
    fun provideAddReminder(reminderDao: ReminderDao): AddReminderUseCase {
        return AddReminderUseCase(reminderDao = reminderDao)
    }

    @ViewModelScoped
    @Provides
    fun provideGetAllReminders(reminderDao: ReminderDao): GetAllRemindersUseCase {
        return GetAllRemindersUseCase(remindersDao = reminderDao)
    }

    @ViewModelScoped
    @Provides
    fun provideEditReminder(reminderDao: ReminderDao): EditReminderUseCase {
        return EditReminderUseCase(reminderDao)
    }

    @ViewModelScoped
    @Provides
    fun retrieveReminder(reminderDao: ReminderDao): RetrieveReminderUseCase {
        return RetrieveReminderUseCase(reminderDao)
    }
}