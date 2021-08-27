package com.example.locationreminder.cache.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.locationreminder.domain.Reminder

@Entity(tableName = "reminders")
data class ReminderEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val title: String,
    val description: String,
    val location: String
)

fun ReminderEntity.toReminder(): Reminder {
    return Reminder(
        id = id,
        title = title,
        description = description,
        location_name = location
    )
}

fun List<ReminderEntity>.toReminderList(): List<Reminder> {
    return map { it.toReminder() }
}
