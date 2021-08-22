package com.example.locationreminder.presentation.ui.reminders.reminders_edit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.locationreminder.R
import com.example.locationreminder.databinding.FragmentReminderEditBinding
import com.example.locationreminder.presentation.ui.reminders.reminders_edit.ReminderEditEvent.AddNewReminderEvent
import com.example.locationreminder.presentation.ui.reminders.reminders_edit.ReminderEditEvent.EditCurrentReminderEvent
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReminderEditFragment : Fragment(), OnMapReadyCallback {

    private val viewModel: ReminderEditViewModel by viewModels()
    private lateinit var binding: FragmentReminderEditBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.fragment_reminder_edit,
            container,
            false
        )
        viewModel.currentReminder = requireArguments().getParcelable("currentReminder")!!
        binding.lifecycleOwner = viewLifecycleOwner
        binding.currentReminder = viewModel.currentReminder

        binding.fabSave.setOnClickListener {
            viewModel.currentReminder.id.let { id ->
                when (id) {
                    0L -> viewModel.onTriggerEvent(AddNewReminderEvent)
                    else -> viewModel.onTriggerEvent(EditCurrentReminderEvent)
                }
            }
        }
        viewModel.snackbarMessage.observe(viewLifecycleOwner) { message ->
            if (message.isNotBlank()) {
                Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
                viewModel.onSnackbarMessageDisplayed()
            }
        }
        viewModel.eventSuccess.observe(viewLifecycleOwner) { isSuccessful ->
            if (isSuccessful) {
                findNavController().popBackStack()
                viewModel.onEventSuccessHandled()
            }
        }
        // MapView
        binding.locationMap.onCreate(savedInstanceState)
        binding.locationMap.getMapAsync(this)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onMapReady(map: GoogleMap) {
        val home = LatLng(36.80627605909481, -76.50982689985706)
        map.addMarker(MarkerOptions().position(home).title("My house"))
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(home, 15f))
    }


    /**
     * We need to forward all of the lifecycle events to the MapView so it can behave properly.
     */
    override fun onResume() {
        binding.locationMap.onResume()
        super.onResume()
    }

    override fun onPause() {
        binding.locationMap.onPause()
        super.onPause()
    }

    override fun onDestroy() {
        binding.locationMap.onDestroy()
        super.onDestroy()
    }

    override fun onLowMemory() {
        binding.locationMap.onLowMemory()
        super.onLowMemory()
    }

    override fun onStart() {
        binding.locationMap.onStart()
        super.onStart()
    }

    override fun onStop() {
        binding.locationMap.onStop()
        super.onStop()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        binding.locationMap.onSaveInstanceState(outState)
        super.onSaveInstanceState(outState)
    }
}