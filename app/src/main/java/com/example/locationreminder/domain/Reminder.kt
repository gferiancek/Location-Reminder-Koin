package com.example.locationreminder.domain

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.library.baseAdapters.BR
import com.example.locationreminder.cache.model.ReminderEntity
import com.google.android.gms.maps.model.LatLng

data class Reminder(
    var id: Long = 0L,
    var title: String = "",
    var description: String = "",
    var latLng: LatLng = LatLng(0.0, 0.0),
    var location_name: String = "",
    var geofence_radius: Float = 100f,
    private var _expiration_interval: Int = 0,
    private var _expiration_duration: Float = 1f,
    private var _transitionType: Int = 3
) : BaseObservable() {
    fun missingData(): Boolean = title.isBlank() || description.isBlank()

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

    var transitionType: Int
        @Bindable get() = _transitionType
        set(value) {
            _transitionType = value
            notifyPropertyChanged(BR.transitionType)
        }
}

fun Reminder.toReminderEntity(): ReminderEntity {
    return ReminderEntity(
        id = id,
        title = title,
        description = description,
        location = location_name
    )
}