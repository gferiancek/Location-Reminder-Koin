package com.udacity.project4.presentation.adapters

import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.slider.Slider
import com.udacity.project4.R
import com.udacity.project4.domain.model.Reminder

@BindingAdapter("bindReminders")
fun RecyclerView.bindReminders(reminderList: List<Reminder>?) {
    reminderList?.let {
        val adapter = adapter as ReminderAdapter
        adapter.submitList(reminderList)
    }
}

@BindingAdapter("headerText")
fun TextView.setHeaderText(isEditing: Boolean) {
    text = when (isEditing) {
        true -> resources.getString(R.string.header_edit_request)
        else -> resources.getString(R.string.header_add_request)
    }
}

@InverseBindingAdapter(attribute = "android:value")
fun Slider.getSliderValue() = value

@BindingAdapter("android:valueAttrChanged")
fun Slider.setSliderListeners(attrChange: InverseBindingListener) {
    addOnChangeListener { _, _, _ ->
        attrChange.onChange()
    }
}

@BindingAdapter("setCustomAdapter")
fun Spinner.setCustomAdapter(enabled: Boolean) {
    if (enabled) {
        val transitionArray = resources.getStringArray(R.array.spinner_entries_transition_types)
        adapter = object : ArrayAdapter<String>(
            context,
            android.R.layout.simple_dropdown_item_1line,
            transitionArray
        ) {
            override fun getCount(): Int {
                return transitionArray.size - 1
            }
        }
        setSelection(transitionArray.size - 1)
    }
}

@BindingAdapter(value = ["interval", "duration"])
fun TextView.setDuration(interval: Int, duration: Float) {
    text = when (interval) {
        0 -> ""
        else -> duration.toInt().toString()
    }
}