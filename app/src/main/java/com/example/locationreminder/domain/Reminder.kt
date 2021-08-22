package com.example.locationreminder.domain

import android.os.Parcelable
import com.example.locationreminder.cache.model.ReminderEntity
import kotlinx.parcelize.Parcelize

@Parcelize
data class Reminder(
    var id: Long = 0L,
    var title: String = "",
    var description: String = "",
    var location: String = ""
) : Parcelable {
    fun missingData(): Boolean = title.isBlank() || description.isBlank()
}

fun Reminder.toReminderEntity(): ReminderEntity {
    return ReminderEntity(
        id = id,
        title = title,
        description = description,
        location = location
    )
}