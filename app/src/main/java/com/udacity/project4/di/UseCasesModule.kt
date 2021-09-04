package com.udacity.project4.di

import com.udacity.project4.data.repository.ReminderRepository
import com.udacity.project4.domain.use_cases.reminders_edit.AddReminderUseCase
import com.udacity.project4.domain.use_cases.reminders_edit.EditReminderUseCase
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
    fun provideAddReminder(repository: ReminderRepository): AddReminderUseCase {
        return AddReminderUseCase(repository = repository)
    }

    @ViewModelScoped
    @Provides
    fun provideGetAllReminders(repository: ReminderRepository): GetAllRemindersUseCase {
        return GetAllRemindersUseCase(repository = repository)
    }

    @ViewModelScoped
    @Provides
    fun provideEditReminder(repository: ReminderRepository): EditReminderUseCase {
        return EditReminderUseCase(repository = repository)
    }
}