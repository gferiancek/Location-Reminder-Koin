package com.udacity.project4.di

import com.udacity.project4.presentation.ui.reminders.reminders_edit.ReminderEditViewModel
import com.udacity.project4.presentation.ui.reminders.reminders_list.RemindersListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { RemindersListViewModel(get()) }
    viewModel { ReminderEditViewModel(get(), get()) }
}
