package com.example.presentation.ui.fragments

import android.Manifest
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Geocoder
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.presentation.R
import com.example.presentation.databinding.FragmentGoogleMapBinding
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import java.io.IOException
import java.util.*


class GoogleMapFragment : Fragment(R.layout.fragment_google_map), OnMapReadyCallback {

    private lateinit var locationRequest: LocationRequest
    private lateinit var googleMap: GoogleMap
    private var isPermissionGranter: Boolean? = null
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private val binding by viewBinding(FragmentGoogleMapBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialize()
        checkPermission()
//        checkLocationPermission()
    }

    private fun initialize() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext())
        val mapFragment = childFragmentManager
            .findFragmentById(R.id.maps) as SupportMapFragment
        mapFragment.getMapAsync {
            val karabalta = LatLng(42.82112116502111, 73.84416404971903)
            it.addMarker(MarkerOptions().position(karabalta).title("Кара-Балта"))
            it.moveCamera(CameraUpdateFactory.newLatLngZoom(karabalta, 13f))
        }
    }

    private fun checkPermission() {
        binding.btnStart.setOnClickListener {
            checkLocationPermission()
            Dexter.withContext(requireContext()).withPermission(Manifest.permission.ACCESS_FINE_LOCATION).withListener(object : PermissionListener {
                override fun onPermissionGranted(response: PermissionGrantedResponse) {
                    isPermissionGranter = true
                    Toast.makeText(requireContext(), "Permission Granter", Toast.LENGTH_SHORT).show()
                    checkLocationPermission()
                }

                override fun onPermissionDenied(response: PermissionDeniedResponse) {
//                    val  intent : Intent = Intent()
//                    intent.action = Settings.ACTION_APPLICATION_SETTINGS
//                    val uri : Uri = Uri.fromParts("package" , "" , "")
//                    intent.data = uri
//                    startActivity(intent)
                    if (!shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                        Toast.makeText(requireContext(), "Checked", Toast.LENGTH_SHORT).show()
                    }
                    Toast.makeText(requireContext(), "Denied", Toast.LENGTH_SHORT).show()
                }

                override fun onPermissionRationaleShouldBeShown(permission: PermissionRequest?, token: PermissionToken?) {
                    token?.continuePermissionRequest()
                    Toast.makeText(requireContext(), "Now", Toast.LENGTH_SHORT).show()
                }
            }).check()
        }

    }

    private fun checkLocationPermission() {
        checkGPS()
//        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//            // Already Grant
//            checkGPS()
//
//        } else {
//            //Denied
//
//            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 100)
//        }
    }
    private fun checkGPS() {
        locationRequest = LocationRequest.create()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 5000
        locationRequest.fastestInterval = 2000

        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)

        builder.setAlwaysShow(true)

        val result = LocationServices.getSettingsClient(
            requireContext()
        )

            .checkLocationSettings(builder.build())

        result.addOnCompleteListener{ task ->

            try {
                val response = task.getResult(
                    ApiException::class.java
                )

                getUserLocation()
            }
            catch (e : ApiException){
                e.printStackTrace()

                when(e.statusCode){
                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> try {

                        val resolveApiException = e as ResolvableApiException
                        resolveApiException.startResolutionForResult(requireActivity() , 200)

                    }catch (sendIntentException : IntentSender.SendIntentException){

                    }

                    LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {

                    }
                }
            }
        }
    }
    private fun getUserLocation() {

        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            return
        }
        fusedLocationProviderClient.lastLocation.addOnCompleteListener{ task ->
            val location = task.result

            if (location != null){
                try {

                    val geoCoder = Geocoder(requireContext() , Locale.getDefault())

                    val address = geoCoder.getFromLocation(location.latitude, location.longitude, 1)

                    val addres_line = address?.get(0)?.getAddressLine(0)

                    Toast.makeText(requireContext(), addres_line, Toast.LENGTH_SHORT).show()

                    val addres_location = address?.get(0)?.getAddressLine(0)

                }catch (
                    e : IOException
                ){

                }
            }
        }
    }

    override fun onMapReady(map: GoogleMap) {

    }
}
