package com.udacity.project4.presentation.ui.reminders.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.udacity.project4.R
import com.udacity.project4.databinding.FragmentReminderDetailBinding
import com.udacity.project4.domain.model.Reminder
import com.udacity.project4.presentation.ui.reminders.reminders_edit.MapUtils

class ReminderDetailFragment : Fragment(), OnMapReadyCallback {

    private lateinit var reminder: Reminder
    private lateinit var binding: FragmentReminderDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.fragment_reminder_detail,
            container,
            false
        )
        reminder = requireArguments().getParcelable<Reminder>("currentReminder")!!
        binding.reminder = reminder

        binding.mvDetailMap.onCreate(savedInstanceState)
        binding.mvDetailMap.getMapAsync(this)
        return binding.root
    }

    override fun onMapReady(map: GoogleMap) {
        val latLng = LatLng(reminder.latitude, reminder.longitude)
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))

        val circle = MapUtils.createCircle(
            map,
            latLng,
            requireContext()
        )
        val marker = MapUtils.createMarker(
            map,
            latLng
        )
        MapUtils.createFadeInAnimation(
            circle,
            marker,
            reminder.geofence_radius
        ).start()
        map.setOnMapClickListener { }
    }

    override fun onResume() {
        binding.mvDetailMap.onResume()
        super.onResume()
    }

    override fun onPause() {
        binding.mvDetailMap.onPause()
        super.onPause()
    }

    override fun onDestroy() {
        binding.mvDetailMap.onDestroy()
        super.onDestroy()
    }

    override fun onLowMemory() {
        binding.mvDetailMap.onLowMemory()
        super.onLowMemory()
    }

    override fun onStart() {
        binding.mvDetailMap.onStart()
        super.onStart()
    }

    override fun onStop() {
        binding.mvDetailMap.onStop()
        super.onStop()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        binding.mvDetailMap.onSaveInstanceState(outState)
        super.onSaveInstanceState(outState)
    }
}