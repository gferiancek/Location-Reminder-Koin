package com.example.locationreminder.presentation.ui.reminders.reminders_edit

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.locationreminder.R
import com.example.locationreminder.databinding.FragmentReminderEditBinding
import com.example.locationreminder.domain.Reminder
import com.example.locationreminder.presentation.ui.reminders.reminders_edit.ReminderEditEvent.AddNewReminderEvent
import com.example.locationreminder.presentation.ui.reminders.reminders_edit.ReminderEditEvent.EditCurrentReminderEvent
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ReminderEditFragment : Fragment(), OnMapReadyCallback {

    private val editViewModel: ReminderEditViewModel by viewModels()
    private lateinit var map: GoogleMap
    private lateinit var binding: FragmentReminderEditBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback

    /**
     * This launcher is called when the user has device location turned on, prompts them to turn on
     * location, and acts based on their decision.
     */
    private val locationResolutionLauncher = registerForActivityResult(
        ActivityResultContracts.StartIntentSenderForResult()
    ) { activityResult ->
        if (activityResult.resultCode == RESULT_OK) {
            enableOrRequestFineLocation()
        } else {
            editViewModel.displayNewSnackbar(getString(R.string.location_off_or_denied))
        }
    }

    /**
     * This launcher is called when the user has not approved fine location permissions, prompts them to
     * approve the permission, and acts based on their decision
     */
    private val fineLocationLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            enableOrRequestFineLocation()
        } else {
            editViewModel.displayNewSnackbar(getString(R.string.location_off_or_denied))
        }
    }

    private val backgroundLocationLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            // Start Geofencing
        } else {
            editViewModel.displayNewSnackbar("Background location is required to trigger geofence notifications. Please enable it in the app Settings.")
        }
    }

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
        if (editViewModel.currentReminder == null) {
            val currentReminder = requireArguments().getParcelable<Reminder>("currentReminder")
            editViewModel.currentReminder = currentReminder
        }
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = editViewModel
        }
        setupListenersAndObservers()

        binding.locationMap.onCreate(savedInstanceState)
        binding.locationMap.getMapAsync(this)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        // If the user's location is turned off, then it is likely that the MapView/Permissions will finish
        // initializing before a new lastLocation is established.  This callback lets us move the camera
        // to the user's position once it is ready.
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                val lastLocation = locationResult.lastLocation
                val latLng = LatLng(lastLocation.latitude, lastLocation.longitude)
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17f))
                fusedLocationClient.removeLocationUpdates(locationCallback)
            }
        }
        checkLocationSettings()
        return binding.root
    }

    /**
     * Function that sets Listeners and observes fields in the viewModel.  Extracted out of onCreate
     * to get rid of clutter.
     */
    private fun setupListenersAndObservers() {
        binding.fabSave.setOnClickListener {
            editViewModel.currentReminder?.id.let { id ->
                when (id) {
                    0L -> editViewModel.onTriggerEvent(AddNewReminderEvent)
                    else -> editViewModel.onTriggerEvent(EditCurrentReminderEvent)
                }
            }
        }
        editViewModel.snackbarMessage.observe(viewLifecycleOwner) { message ->
            if (message.isNotBlank()) {
                Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).show()
                editViewModel.onSnackbarMessageDisplayed()
            }
        }
        editViewModel.eventSuccess.observe(viewLifecycleOwner) { isSuccessful ->
            if (isSuccessful) {
                findNavController().popBackStack()
                editViewModel.onEventSuccessHandled()
            }
        }
    }

    /**
     * Once the map is ready, we setup universal functionality in the MapView.  Location restricted features
     * are set in the requestFineLocation() function and launcher.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        map.setMapStyle(
            MapStyleOptions.loadRawResourceStyle(
                requireContext(),
                R.raw.map_style
            )
        )
        map.setOnPoiClickListener { poi ->
            map.addMarker(
                MarkerOptions()
                    .position(poi.latLng)
                    .title(poi.name)
            )?.showInfoWindow()
        }
    }

    private fun createLocationRequest(): LocationRequest {
        return LocationRequest.create().apply {
            interval = 10000
            fastestInterval = 5000
            priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
        }
    }

    /**
     * Checks if users device location is turn on. If enabled, checks fine Location permissions.
     * If disabled, starts the launcher to prompt the user to turn on device location.
     */
    private fun checkLocationSettings() {
        val locationRequestBuilder = LocationSettingsRequest.Builder()
            .addLocationRequest(createLocationRequest())
        val client: SettingsClient = LocationServices.getSettingsClient(requireContext())
        val task: Task<LocationSettingsResponse> =
            client.checkLocationSettings(locationRequestBuilder.build())
        task.addOnSuccessListener {
            enableOrRequestFineLocation()
        }
        task.addOnFailureListener { exception ->
            if (exception is ResolvableApiException) {
                val intentSenderRequest = IntentSenderRequest.Builder(exception.resolution)
                locationResolutionLauncher.launch(intentSenderRequest.build())
            }
        }
    }

    /**
     * Checks if user has enabled the fine location permission.  If enabled, adds location capabilities
     * to our MapView. If disabled, starts launcher to request the permission.
     */
    private fun enableOrRequestFineLocation() {
        val fineLocation = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        when (fineLocation) {
            true -> {
                map.isMyLocationEnabled = true
                fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                    if (location != null) {
                        val latLng = LatLng(location.latitude, location.longitude)
                        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17f))
                    } else {
                        editViewModel.displayNewSnackbar("Fetching current location data.")
                        fusedLocationClient.requestLocationUpdates(
                            createLocationRequest(),
                            locationCallback,
                            Looper.getMainLooper()
                        )
                    }
                }
            }
            false -> fineLocationLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    /**
     * Helper functions used for requesting location permissions
     */
    @RequiresApi(Build.VERSION_CODES.Q)
    private fun requestBackgroundLocation() {
        val backgroundLocation = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_BACKGROUND_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        when (backgroundLocation) {
            true -> {
                // Start geofencing
            }
            false -> backgroundLocationLauncher.launch(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
        }
    }

    /**
     * We need to forward all of the lifecycle events to the MapView so it can behave properly. Needed since
     * we aren't using a SupportMapFragment. MapView.onCreate() was already forwarded in fragments
     * onCreateView()
     */
    override fun onResume() {
        binding.locationMap.onResume()
        super.onResume()
    }

    override fun onPause() {
        binding.locationMap.onPause()
        fusedLocationClient.removeLocationUpdates(locationCallback)
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