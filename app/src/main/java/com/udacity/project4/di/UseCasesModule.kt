package com.udacity.project4.di

import com.udacity.project4.use_cases.reminders_edit.AddReminderUseCase
import com.udacity.project4.use_cases.reminders_edit.EditReminderUseCase
import com.udacity.project4.use_cases.reminders_list.GetAllRemindersUseCase
import org.koin.dsl.module


val useCasesModule = module {
    factory { GetAllRemindersUseCase(get()) }
    factory { AddReminderUseCase(get()) }
    factory { EditReminderUseCase(get()) }
}
