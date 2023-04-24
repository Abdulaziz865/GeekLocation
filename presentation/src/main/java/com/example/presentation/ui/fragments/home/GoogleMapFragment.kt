package com.example.presentation.ui.fragments.home

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.presentation.R
import com.example.presentation.databinding.FragmentGoogleMapBinding
import com.example.presentation.extensions.accessFineLocationAsk
import com.example.presentation.extensions.bothAsk
import com.example.presentation.extensions.nullAsk
import com.example.presentation.extensions.writeExternalStorageAsk
import com.example.presentation.utils.GpsStatus
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GoogleMapFragment : Fragment(R.layout.fragment_google_map), OnMapReadyCallback , GoogleMap.OnMarkerClickListener{

    private lateinit var mMap: GoogleMap
    private lateinit var lastLocation: Location
    private lateinit var locationRequest: LocationRequest
    private var fusedLocationProviderClient: FusedLocationProviderClient? = null
    private val viewModel: GoogleMapViewModel by viewModels()
    private val binding by viewBinding(FragmentGoogleMapBinding::bind)
    private val gpsObserver = Observer<GpsStatus> { status ->
        updateGpsCheckUI(status)
    }
    companion object {
        private const val LOCATION_REQUEST_CODE = 1
    }
    private val requestPermission =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->

            var isDenied = false
            var isDontAsk = nullAsk
            var isBoath = false

            val accessFineLocation = permissions[Manifest.permission.ACCESS_FINE_LOCATION]
            val writeExternalStorage = permissions[Manifest.permission.WRITE_EXTERNAL_STORAGE]

            if (accessFineLocation == true) {
                val gpsEnabled =
                    requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
                if (gpsEnabled.isProviderEnabled(LocationManager.GPS_PROVIDER) && ContextCompat.checkSelfPermission(
                        requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    binding.btnStart.isVisible = false
                    binding.closerCardView.isVisible = false
                }
            }
            else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION
                    )
                ) {

                    isDenied = true
                }
                else {

                    isDontAsk = accessFineLocationAsk
                    isBoath = true
                }
            }

            if (writeExternalStorage == true) {
                val gpsEnabled =
                    requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
                if (gpsEnabled.isProviderEnabled(LocationManager.GPS_PROVIDER)
                    && ContextCompat.checkSelfPermission(
                        requireContext(), Manifest.permission.ACCESS_FINE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    binding.btnStart.isVisible = false
                    binding.closerCardView.isVisible = false
                }
            }
            else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        requireActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE
                    )
                ) {
                    isDenied = true
                }
                else {
                    isDontAsk = writeExternalStorageAsk

                    if (isBoath){
                        isDontAsk = bothAsk
                    }
                }
            }

            if (isDenied) {
                val alertDialog: AlertDialog.Builder =
                    AlertDialog.Builder(requireContext(), R.style.AlertDialogTheme)

                alertDialog.setMessage(getString(R.string.denial_warning))
                    .setTitle(getString(R.string.attention_title))

                alertDialog.setPositiveButton(getString(R.string.provide_button)) { dialog, which ->
                    requestStoragePermission()
                }
                alertDialog.setNegativeButton(getString(R.string.cansel_button)) { dialog, which ->
                    Log.e("permissions", "Permission Denied")
                }
                alertDialog.show()
            }

            when (isDontAsk) {
                bothAsk -> {
                    Snackbar.make(
                        binding.btnStart,
                        getString(R.string.dont_show_again_button) + " Оба",
                        Snackbar.LENGTH_LONG
                    ).setAction(getString(R.string.settings_action_button)) {
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                        val uri: Uri = Uri.fromParts(
                            getString(R.string.packages), activity?.packageName, null
                        )
                        intent.data = uri
                        startActivity(intent)
                    }
                        .setActionTextColor(Color.parseColor(getString(R.string.color_settings_button)))
                        .show()
                }
                accessFineLocationAsk -> {
                    Snackbar.make(
                        binding.btnStart,
                        getString(R.string.dont_show_again_button) + " Местоположение",
                        Snackbar.LENGTH_LONG
                    ).setAction(getString(R.string.settings_action_button)) {
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                        val uri: Uri = Uri.fromParts(
                            getString(R.string.packages), activity?.packageName, null
                        )
                        intent.data = uri
                        startActivity(intent)
                    }
                        .setActionTextColor(Color.parseColor(getString(R.string.color_settings_button)))
                        .show()
                }
                writeExternalStorageAsk -> {
                    Snackbar.make(
                        binding.btnStart,
                        getString(R.string.dont_show_again_button) + " Читание Данных",
                        Snackbar.LENGTH_LONG
                    ).setAction(getString(R.string.settings_action_button)) {
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                        val uri: Uri = Uri.fromParts(
                            getString(R.string.packages), activity?.packageName, null
                        )
                        intent.data = uri
                        startActivity(intent)
                    }
                        .setActionTextColor(Color.parseColor(getString(R.string.color_settings_button)))
                        .show()
                }
                else -> {
                    initialize()
                }
            }
        }
    private fun updateGpsCheckUI(status: GpsStatus) = with(binding) {
        when (status) {
            is GpsStatus.Enabled -> {
                if (ContextCompat.checkSelfPermission(
                        requireContext(), Manifest.permission.ACCESS_FINE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                        requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    Log.e("gpsEnabled", "ON")
                    btnStart.isVisible = false
                    closerCardView.isVisible = false
                }
            }

            is GpsStatus.Disabled -> {
                Log.e("gpsEnabled", "OFF")
                btnStart.isVisible = true
                closerCardView.isVisible = true
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialize()
        subscribeToGpsListener()
        checkPermission()
    }

    private fun initialize() {
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireContext())

        val mapFragment = childFragmentManager.findFragmentById(R.id.maps) as SupportMapFragment

        mapFragment.getMapAsync {
            mMap = it
            mMap.uiSettings.isZoomControlsEnabled = true
            mMap.setOnMarkerClickListener(this)

            if (ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    LOCATION_REQUEST_CODE
                )
                return@getMapAsync
            }
            mMap.isMyLocationEnabled = true
            fusedLocationProviderClient?.lastLocation?.addOnSuccessListener(requireActivity()) { location ->
                if (location != null) {
                    lastLocation = location
                    val currentLatLong = LatLng(location.latitude, location.longitude)
                    placeMarkerOnMap(currentLatLong)
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLong, 13f))
                }
            }
        }
    }

    private fun subscribeToGpsListener() =
        viewModel.gpsStatusLiveData(requireActivity()).observe(viewLifecycleOwner, gpsObserver)

    private fun checkPermission() {
        binding.btnStart.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                    requireActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestStoragePermission()
            } else {
                Log.e("permissions", "Always Granted")
                checkGPS()
            }
        }
    }

    private fun checkGPS() {
        locationRequest = LocationRequest.Builder(5000)
            .setGranularity(Granularity.GRANULARITY_FINE)
            .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
            .setMinUpdateDistanceMeters(100F)
            .build()

        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)
            .build()

        val task: Task<LocationSettingsResponse> =
            LocationServices.getSettingsClient(requireContext()).checkLocationSettings(builder)

        task.addOnCompleteListener {

            try {
                it.getResult(ApiException::class.java)

            } catch (e: ApiException) {

                if (e.statusCode == LocationSettingsStatusCodes.RESOLUTION_REQUIRED) {
                    val resolvableApiException: ResolvableApiException = e as ResolvableApiException
                    try {
                        resolvableApiException.startResolutionForResult(
                            requireActivity(), 101
                        )
                    } catch (sendIntentException: IntentSender.SendIntentException) {
                        sendIntentException.printStackTrace()
                    }
                }

                if (e.statusCode == LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE) {
                    Log.e("setting", "Setting not available")
                }
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
    }

    private fun placeMarkerOnMap(currentLatLong: LatLng) {
        val markerOptions = MarkerOptions().position(currentLatLong)
        markerOptions.title("Я")
        mMap.addMarker(markerOptions)
    }

    private fun requestStoragePermission() {
        requestPermission.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        )
    }

    override fun onMarkerClick(p0: Marker) = false
}
