package com.example.locationreminder.presentation.ui.reminders.reminders_edit

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import androidx.core.content.ContextCompat
import com.example.locationreminder.R
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.*

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
}