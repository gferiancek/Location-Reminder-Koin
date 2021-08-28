package com.example.locationreminder.cache.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.locationreminder.domain.model.Reminder

@Entity(tableName = "reminders")
data class ReminderEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val description: String,
    val latitude: Double,
    val longitude: Double,
    val location_name: String,
    val geofence_radius: Float,
    val expiration_interval: Int,
    val expiration_duration: Float,
    val transition_type: Int
)

fun ReminderEntity.toReminder(): Reminder {
    return Reminder(
        id = id,
        title = title,
        description = description,
        latitude = latitude,
        longitude = longitude,
        location_name = location_name,
        geofence_radius = geofence_radius,
        _expiration_interval = expiration_interval,
        _expiration_duration = expiration_duration,
        _transitionType = transition_type
    )
}

fun List<ReminderEntity>.toReminderList(): List<Reminder> {
    return map { it.toReminder() }
}
