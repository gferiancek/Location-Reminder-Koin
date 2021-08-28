package com.example.locationreminder.presentation.ui.reminders.reminders_edit

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import androidx.core.content.ContextCompat
import com.example.locationreminder.R
import com.example.locationreminder.presentation.ui.reminders.reminders_edit.MapUtils.MillisConversions.*
import com.google.android.gms.location.Geofence
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.*
import java.util.concurrent.TimeUnit

object MapUtils {
    fun createCircle(
        map: GoogleMap,
        latLng: LatLng,
        context: Context
    ): Circle {
        return map.addCircle(
            CircleOptions()
                .center(latLng)
                .radius(0.0)
                .strokeWidth(2f)
                .strokeColor(
                    ContextCompat.getColor(
                        context,
                        R.color.map_overlay_outline
                    )
                )
                .fillColor(
                    ContextCompat.getColor(
                        context,
                        R.color.map_overlay_fill
                    )
                )
        )
    }

    fun createMarker(
        map: GoogleMap,
        latLng: LatLng
    ): Marker {
        return map.addMarker(
            MarkerOptions()
                .alpha(0f)
                .position(latLng)
        )!!
    }

    fun createFadeOutAnimation(
        circle: Circle,
        marker: Marker,
    ): AnimatorSet {
        val circleFade = createRadiusAnimation(circle, 0f)
        val markerFade = ObjectAnimator.ofFloat(marker, "alpha", 0f).apply {
            duration = 500
        }
        return AnimatorSet().apply { play(circleFade).with(markerFade) }
    }

    fun createFadeInAnimation(
        circle: Circle,
        marker: Marker,
        targetRadius: Float
    ): AnimatorSet {
        val circleFade = createRadiusAnimation(circle, targetRadius)
        val markerFadeIn = ObjectAnimator.ofFloat(marker, "alpha", 1f).apply {
            duration = 500
        }
        return AnimatorSet().apply { play(circleFade).with(markerFadeIn) }
    }

    fun createRadiusAnimation(circle: Circle, targetRadius: Float): ValueAnimator {
        return ValueAnimator.ofFloat(circle.radius.toFloat(), targetRadius).apply {
            duration = 500
            addUpdateListener { circle.radius = (it.animatedValue as Float).toDouble() }
        }
    }

    fun convertExpirationToMs(interval: Int, duration: Float): Long {
        val durationLong = (interval * duration).toLong()
        return when (interval) {
            1 -> TimeUnit.MINUTES.toMillis(durationLong)
            2 -> TimeUnit.HOURS.toMillis(durationLong)
            3 -> TimeUnit.DAYS.toMillis(durationLong)
            4 -> (duration * WEEK().ratio).toLong()
            5 -> (duration * MONTH().ratio).toLong()
            6 -> (duration * YEAR().ratio).toLong()
            else -> Geofence.NEVER_EXPIRE
        }
    }

    /**
     * Java TimeUnit doesn't have weeks or above, so we need our own values!
     */
    sealed class MillisConversions {
        data class WEEK(val ratio: Long = 604800000)
        data class MONTH(val ratio: Long = 2628000000)
        data class YEAR(val ratio: Long = 31540000000)
    }

    fun getTransitionConstant(position: Int): Int {
        return when (position) {
            0 -> Geofence.GEOFENCE_TRANSITION_ENTER
            1 -> Geofence.GEOFENCE_TRANSITION_EXIT
            2 -> Geofence.GEOFENCE_TRANSITION_DWELL
            else -> Geofence.GEOFENCE_TRANSITION_ENTER
        }
    }
}