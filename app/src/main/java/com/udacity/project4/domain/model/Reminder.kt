package com.udacity.project4.domain.model

import android.os.Parcelable
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.library.baseAdapters.BR
import com.udacity.project4.cache.model.ReminderEntity
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class Reminder(
    var id: String = UUID.randomUUID().toString(),
    var title: String = "",
    var description: String = "",
    var latitude: Double = 0.0,
    var longitude: Double = 0.0,
    var location_name: String = "",
    var geofence_radius: Float = 150f,
    private var _expiration_interval: Int = 0,
    private var _expiration_duration: Float = 1f,
    var transition_type: Int = 3
) : BaseObservable(), Parcelable {

    fun hasBlankTextFields(): Boolean = title.isBlank() ||
            description.isBlank() ||
            location_name.isBlank()

    /**
     * Three @Bindable getter/setters are used to notify the layout of changes so it can update.
     */
    var expirationInterval: Int
        @Bindable get() = _expiration_interval
        set(value) {
            _expiration_interval = value
            notifyPropertyChanged(BR.expirationInterval)
        }

    var expirationDuration: Float
        @Bindable get() = _expiration_duration
        set(value) {
            _expiration_duration = value
            notifyPropertyChanged(BR.expirationDuration)
        }
}

fun Reminder.toReminderEntity(): ReminderEntity {
    return ReminderEntity(
        id = id,
        title = title,
        description = description,
        latitude = latitude,
        longitude = longitude,
        location_name = location_name,
        geofence_radius = geofence_radius,
        expiration_interval = expirationInterval,
        expiration_duration = expirationDuration,
        transition_type = transition_type
    )
}