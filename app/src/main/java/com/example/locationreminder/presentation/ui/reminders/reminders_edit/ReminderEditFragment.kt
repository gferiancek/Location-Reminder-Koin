package com.example.locationreminder.presentation.ui.reminders.reminders_edit

import android.Manifest
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.core.animation.doOnEnd
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.locationreminder.R
import com.example.locationreminder.databinding.FragmentReminderEditBinding
import com.example.locationreminder.presentation.ui.reminders.reminders_edit.ReminderEditEvent.AddNewReminderEvent
import com.example.locationreminder.presentation.ui.reminders.reminders_edit.ReminderEditEvent.EditCurrentReminderEvent
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.*
import com.google.android.gms.tasks.Task
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.*
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ReminderEditFragment : Fragment(), OnMapReadyCallback {

    private val editViewModel: ReminderEditViewModel by viewModels()
    private lateinit var map: GoogleMap
    private lateinit var circleOverlay: Circle
    private lateinit var activeMarker: Marker
    private lateinit var binding: FragmentReminderEditBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var geofencingClient: GeofencingClient
    private lateinit var geofenceSheet: BottomSheetBehavior<CardView>
    private var radiusAnimator = ValueAnimator()

    /**
     * Launchers and helper function for checking location services/permissions and responding to the user's choice.
     */
    private val locationServicesLauncher = registerForActivityResult(
        ActivityResultContracts.StartIntentSenderForResult()
    ) { result -> if (result.resultCode == RESULT_OK) requestLocation() else findNavController().popBackStack() }

    private val courseAndFineLocationLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { results -> if (results.all { it.value == true }) enableLocation() else handleDeniedLocation() }

    private val backgroundLocationLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted -> if (isGranted) saveReminder() else handleDeniedLocation() }

    /**
     * Location permissions are asked for incrementally, but if any of them are denied, we jump straight
     * to asking the user to enable background location.  This way, we explain up front why we need
     * access and the user following the request will green light us for the rest of the app flow.
     */
    private fun handleDeniedLocation() {
        AlertDialog.Builder(requireContext())
            .setMessage(getString(R.string.location_rationale))
            .setTitle(getString(R.string.location_dialog_title))
            .setPositiveButton("Settings") { _, _ ->
                val settingsIntent = Intent(
                    Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.fromParts(
                        "package",
                        requireContext().packageName,
                        null
                    )
                ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(settingsIntent)
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
                findNavController().popBackStack()
            }.show()
    }

    /**If the user's location is turned off, then it is likely that the MapView/Permissions will finish
     * initializing before a new lastLocation is established.  This callback lets us move the camera
     * to the user's position once it is ready.
     */
    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val lastLocation = locationResult.lastLocation
            val latLng = LatLng(lastLocation.latitude, lastLocation.longitude)
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
            fusedLocationClient.removeLocationUpdates(this)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        geofencingClient = LocationServices.getGeofencingClient(requireContext())
        binding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.fragment_reminder_edit,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            currentReminder = editViewModel.currentReminder
            bottomSheet.currentReminder = editViewModel.currentReminder
            fabSave.setOnClickListener { if (Build.VERSION.SDK_INT >= 29) requestBackgroundLocation() else saveReminder() }
            locationMap.onCreate(savedInstanceState)
            locationMap.getMapAsync(this@ReminderEditFragment)
        }
        editViewModel.apply {
            snackbarMessage.observe(viewLifecycleOwner) { message ->
                if (message.isNotBlank()) {
                    if (message.equals(getString(R.string.no_transition_type))) {
                        geofenceSheet.state = STATE_EXPANDED
                    }
                    val snackBar = Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT)
                    if (geofenceSheet.state == STATE_HIDDEN) {
                        snackBar.anchorView = binding.fabSave
                    }
                    snackBar.setAction(getString(R.string.action_ok)) { snackBar.dismiss() }
                        .show()
                    editViewModel.onSnackbarMessageDisplayed()
                }
            }
            eventSuccess.observe(viewLifecycleOwner) { isSuccessful ->
                if (isSuccessful) {
                    findNavController().popBackStack()
                    editViewModel.onEventSuccessHandled()
                }
            }
        }
        geofenceSheet = from(binding.bottomSheet.cvBottomSheet).apply {
            state = STATE_HIDDEN
            addBottomSheetCallback(object : BottomSheetCallback() {
                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    binding.bottomSheet.tvGeofenceRequestHeader.let { header ->
                        if (newState == STATE_EXPANDED) {
                            header.setCompoundDrawablesWithIntrinsicBounds(
                                0, R.drawable.ic_baseline_expand_less_24, 0, 0
                            )
                        } else if (newState == STATE_COLLAPSED) {
                            header.setCompoundDrawablesWithIntrinsicBounds(
                                0, R.drawable.ic_baseline_expand_up_24, 0, 0
                            )
                        }
                    }
                }

                override fun onSlide(bottomSheet: View, slideOffset: Float) {}
            })
        }
        checkLocationSettings()
    }

    /**
     * Once the map is ready, we setup universal functionality in the MapView.  Location restricted features
     * are set in the requestFineLocation() function and launcher.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap.apply {
            setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                    requireContext(),
                    R.raw.map_style
                )
            )
            editViewModel.currentReminder.let { reminder ->
                circleOverlay = MapUtils.createCircle(
                    this,
                    reminder.latLng,
                    requireContext()
                )
                activeMarker = MapUtils.createMarker(
                    this,
                    reminder.latLng
                )
            }
            setOnMarkerClickListener {
                geofenceSheet.state = when (geofenceSheet.state) {
                    STATE_EXPANDED -> STATE_EXPANDED
                    else -> STATE_COLLAPSED
                }
                true
            }
            setOnPoiClickListener { poi ->
                respondToMapClickEvent(
                    poi.latLng,
                    poi.name
                )
            }
            setOnMapLongClickListener { location ->
                respondToMapClickEvent(
                    LatLng(location.latitude, location.longitude),
                    getString(R.string.dropped_pin)
                )
            }
        }
        // This requires circleOverlay to be initialized so it cannot be setup in onViewCreated.
        binding.bottomSheet.sliderSheetRadius.addOnChangeListener { _, value, _ ->
            if (radiusAnimator.isRunning) radiusAnimator.end()
            radiusAnimator = MapUtils.createRadiusAnimation(circleOverlay, value)
            radiusAnimator.start()
        }
    }

    /**
     * Helper function to setup the map after receiving a click event.
     */
    private fun respondToMapClickEvent(latLng: LatLng, name: String) {
        editViewModel.currentReminder.latLng = latLng
        if (circleOverlay.radius > 0 &&
            activeMarker.alpha > 0
        ) {
            MapUtils.createFadeOutAnimation(
                circleOverlay,
                activeMarker,
            ).apply {
                doOnEnd {
                    map.clear()
                    circleOverlay = MapUtils.createCircle(map, latLng, requireContext())
                    activeMarker = MapUtils.createMarker(map, latLng)
                    MapUtils.createFadeInAnimation(
                        circleOverlay,
                        activeMarker,
                        binding.bottomSheet.sliderSheetRadius.value
                    ).start()
                }
                start()
            }
        } else {
            circleOverlay = MapUtils.createCircle(map, latLng, requireContext())
            activeMarker = MapUtils.createMarker(map, latLng)
            MapUtils.createFadeInAnimation(
                circleOverlay,
                activeMarker,
                binding.bottomSheet.sliderSheetRadius.value
            ).start()
        }
        map.animateCamera(CameraUpdateFactory.newLatLng(latLng))
        binding.bottomSheet.etSheetName.setText(name)
        geofenceSheet.state = STATE_COLLAPSED
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
        task.addOnSuccessListener { requestLocation() }
        task.addOnFailureListener { exception ->
            if (exception is ResolvableApiException) {
                locationServicesLauncher.launch(
                    IntentSenderRequest.Builder(exception.resolution).build()
                )
            }
        }
    }

    /**
     * Helper function to create a location request
     */
    private fun createLocationRequest(): LocationRequest {
        return LocationRequest.create().apply {
            interval = 5000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
    }

    /**
     * Checks if user has enabled the fine location permission.  If enabled, adds location capabilities
     * to our MapView. If disabled, starts launcher to request the permission.
     */
    private fun requestLocation() {
        val fineLocation = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        val coarseLocation = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        when {
            fineLocation && coarseLocation -> enableLocation()
            shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) -> handleDeniedLocation()
            else -> courseAndFineLocationLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    /**
     * Logic to enable location on our MapView. Since the logic is used in two places it has been extracted
     * out to a function.  However, Android Studio now thinks it is being called without checking permissions
     * so we suppress that Lint warning.
     */
    @SuppressLint("MissingPermission")
    private fun enableLocation() {
        map.isMyLocationEnabled = true
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            when {
                location != null -> {
                    val latLng = LatLng(location.latitude, location.longitude)
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
                }
                else -> {
                    editViewModel.displayNewSnackbar(getString(R.string.fetching_location))
                    fusedLocationClient.requestLocationUpdates(
                        createLocationRequest(),
                        locationCallback,
                        Looper.getMainLooper()
                    )
                }
            }
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

        when {
            backgroundLocation -> saveReminder()
            shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_BACKGROUND_LOCATION) -> {
                handleDeniedLocation()
            }
            else -> backgroundLocationLauncher.launch(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
        }
    }

    /**
     * Extracting out logic since it is used in 3 different places.
     */
    private fun saveReminder() {
        when (editViewModel.currentReminder.id) {
            0L -> editViewModel.onTriggerEvent(AddNewReminderEvent)
            else -> editViewModel.onTriggerEvent(EditCurrentReminderEvent)
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