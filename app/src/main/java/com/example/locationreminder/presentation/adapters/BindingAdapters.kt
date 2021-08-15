package com.example.locationreminder.presentation.adapters

import android.view.View
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.locationreminder.R
import com.example.locationreminder.domain.Reminder
import com.google.android.material.progressindicator.CircularProgressIndicator

@BindingAdapter("bindReminders")
fun RecyclerView.bindReminders(reminderList: List<Reminder>?) {
    reminderList?.let {
        val adapter = adapter as ReminderAdapter
        adapter.submitList(reminderList)
    }
}

@BindingAdapter("setContentVisibility")
fun View.setContentVisibility(
    reminderList: List<Reminder>?
) {
    reminderList?.let {
        visibility = when (id) {
            R.id.rv_reminders -> if (it.isNotEmpty()) View.VISIBLE else View.GONE
            else -> if (it.isNotEmpty()) View.GONE else View.VISIBLE
        }
    }
}

@BindingAdapter("setLoadingVisibility")
fun CircularProgressIndicator.setLoadingVisibility(isLoading: Boolean) {
    visibility = when (isLoading) {
        true -> View.VISIBLE
        false -> View.GONE
    }
}