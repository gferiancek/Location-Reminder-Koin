package com.udacity.project4.di

import com.udacity.project4.data.repository.ReminderRepository
import com.udacity.project4.use_cases.reminders_edit.AddReminderUseCase
import com.udacity.project4.use_cases.reminders_edit.EditReminderUseCase
import com.udacity.project4.use_cases.reminders_list.GetAllRemindersUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCasesModule {

    @Singleton
    @Provides
    fun provideAddReminder(repository: ReminderRepository): AddReminderUseCase {
        return AddReminderUseCase(repository = repository)
    }

    @Singleton
    @Provides
    fun provideGetAllReminders(repository: ReminderRepository): GetAllRemindersUseCase {
        return GetAllRemindersUseCase(repository = repository)
    }

    @Singleton
    @Provides
    fun provideEditReminder(repository: ReminderRepository): EditReminderUseCase {
        return EditReminderUseCase(repository = repository)
    }
}