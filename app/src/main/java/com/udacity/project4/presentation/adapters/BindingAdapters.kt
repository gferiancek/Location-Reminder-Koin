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

@BindingAdapter(value = ["enableCustomAdapter", "android:selectedItemPosition"])
fun Spinner.setCustomAdapter(enableCustomAdapter: Boolean, position: Int) {
    if (enableCustomAdapter) {
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
        if (position == 3) {
            setSelection(transitionArray.size - 1)
        }
    }
}

@BindingAdapter(value = ["interval", "duration"])
fun TextView.setDuration(interval: Int, duration: Float) {
    text = when (interval) {
        0 -> ""
        else -> duration.toInt().toString()
    }
}

@BindingAdapter(value = ["interval", "duration", "shouldFormat"])
fun TextView.setFormattedDuration(interval: Int, duration: Float, shouldFormat: Boolean) {
    text = when (interval) {
        0 -> "Never Expires"
        1 -> "Expiration Interval: ${duration.toInt()} Minute(s)"
        2 -> "Expiration Interval: ${duration.toInt()} Hour(s)"
        3 -> "Expiration Interval: ${duration.toInt()} Day(s)"
        4 -> "Expiration Interval: ${duration.toInt()} Week(s)"
        5 -> "Expiration Interval: ${duration.toInt()} Month(s)"
        else -> "Expiration Interval: ${duration.toInt()} Year(s)"
    }
}

@BindingAdapter("formatTransition")
fun TextView.setFormattedTransition(transitionType: Int) {
    text = when (transitionType) {
        0 -> "Transition Type: Enter"
        1 -> "Transition Type: Exit"
        else -> "Transition Type: Dwell"
    }
}

@BindingAdapter(value = ["latitude", "longitude"])
fun TextView.setFormattedLatLng(latitude: Double, longitude: Double) {
    val formattedLatitude = String.format("%.3f", latitude)
    val formattedLongitude = String.format("%.3f", longitude)
    val formattedText = "$formattedLatitude, $formattedLongitude"
    text = formattedText
}