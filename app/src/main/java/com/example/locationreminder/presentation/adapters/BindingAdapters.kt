package com.example.locationreminder.presentation.adapters

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.locationreminder.domain.Reminder

@BindingAdapter("bindReminders")
fun RecyclerView.bindReminders(reminderList: List<Reminder>?) {
    reminderList?.let {
        val adapter = adapter as ReminderAdapter
        adapter.submitList(reminderList)
    }
}