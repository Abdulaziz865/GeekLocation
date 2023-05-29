package com.example.presentation.ui.fragments.home

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.graphics.drawable.ColorDrawable
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.domain.models.LocationModel
import com.example.domain.models.UserCoordinates
import com.example.domain.models.UsersModel
import com.example.presentation.R
import com.example.presentation.databinding.FragmentGoogleMapBinding
import com.example.presentation.extensions.accessFineLocationAsk
import com.example.presentation.extensions.bothAsk
import com.example.presentation.extensions.nullAsk
import com.example.presentation.extensions.writeExternalStorageAsk
import com.example.presentation.utils.GpsStatus
import com.example.presentation.utils.PermissionStatus
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
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GoogleMapFragment : Fragment(R.layout.fragment_google_map), OnMapReadyCallback,
    GoogleMap.OnMarkerClickListener {

    private val db = Firebase.firestore
    private var auth: FirebaseAuth? = null
    private lateinit var mMap: GoogleMap
    private var dialogShowed = false
    private var lastLocation: Location? = null

    //    private var mark: Marker? = null
    private lateinit var locationRequest: LocationRequest
    private var fusedLocationProviderClient: FusedLocationProviderClient? = null
    private val viewModel: GoogleMapViewModel by viewModels()
    private val binding by viewBinding(FragmentGoogleMapBinding::bind)
    private var latitudeLiveData: Double = 0.0
    private var longitudeLiveData: Double = 0.0
    private val gpsObserver = Observer<GpsStatus> { status ->
        updateGpsCheckUI(status)
    }
    private val permissionObserver = Observer<PermissionStatus> { status ->
        updatePermissionCheckUI(status)
    }
    private val locationObserver = Observer<UserCoordinates> { status ->
        latitudeLiveData = status.latitude!!
        longitudeLiveData = status.longitude!!
        Log.e("location", latitudeLiveData.toString())
        Log.e("location", longitudeLiveData.toString())
//        saageg(status)
        checkUserLocation()
        fetchLocationCoordinates()
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
//                    binding.btnStart.isVisible = false
//                    binding.closerCardView.isVisible = false
                }
            }
            else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION
                    )
                ) {
                    updatePermissionCheckUI(PermissionStatus.Denied())
                    isDenied = true
                }
                else {
                    updatePermissionCheckUI(PermissionStatus.Blocked())
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
//                    binding.btnStart.isVisible = false
//                    binding.closerCardView.isVisible = false
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

                    if (isBoath) {
                        isDontAsk = bothAsk
                    }
                }
            }

            if (isDenied) {
                startDialog()
            }

            when (isDontAsk) {
                bothAsk -> {
                    isEnabledDontAskPermission("Включите разрешение на \"Местоположение и Доступ к Медиа\" перейдя в настройки")
                }
                accessFineLocationAsk -> {
                    isEnabledDontAskPermission("Включите разрешение на \"Местоположение\" перейдя в настройки")
                }
                writeExternalStorageAsk -> {
                    isEnabledDontAskPermission("Включите разрешение на \"Доступ к Медиа\" перейдя в настройки")
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
                    mMap.uiSettings.isZoomGesturesEnabled = true
                    mMap.uiSettings.isScrollGesturesEnabled = true
                    mMap.uiSettings.isRotateGesturesEnabled = true
                    mMap.uiSettings.isScrollGesturesEnabledDuringRotateOrZoom = true
                    mMap.uiSettings.isTiltGesturesEnabled = true
                    mMap.uiSettings.isCompassEnabled = true
                    mMap.uiSettings.isMyLocationButtonEnabled = true

                    fusedLocationProviderClient?.lastLocation?.addOnSuccessListener { location ->
                        if (location != null) lastLocation = location
                    }

                    val currentLatLong = lastLocation?.latitude?.let {
                        lastLocation?.longitude?.let { it1 ->
                            LatLng(
                                it, it1
                            )
                        }
                    }
                    currentLatLong.let {
                        it?.let { it1 ->
                            CameraUpdateFactory.newLatLngZoom(
                                it1, 13f
                            )
                        }
                    }.let {
                        if (it != null) {
                            mMap.animateCamera(it)
                        }
                    }
                }
            }

            is GpsStatus.Disabled -> {
                mMap.uiSettings.isZoomGesturesEnabled = false
                mMap.uiSettings.isScrollGesturesEnabled = false
                mMap.uiSettings.isRotateGesturesEnabled = false
                mMap.uiSettings.isScrollGesturesEnabledDuringRotateOrZoom = false
                mMap.uiSettings.isTiltGesturesEnabled = false
                mMap.uiSettings.isCompassEnabled = false
                mMap.uiSettings.isMyLocationButtonEnabled = false
                Log.e("gpsEnabled", "OFF")
                startDialog()
            }
        }
    }

    private fun updatePermissionCheckUI(status: PermissionStatus) {
        when (status) {
            is PermissionStatus.Granted -> {
                Log.e("permission", "Granted")
            }

            is PermissionStatus.Denied -> {
                Log.e("permission", "Denied")
            }

            is PermissionStatus.Blocked -> {
                Log.e("permission", "Blocked")
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialize()
        startDialog()
        setupListener()
        subscribeToGpsListener()
        subscribeToLocationPermissionListener()
        subscribeToLocationLiveData()
        checkUserAccount()
        checkUserLocation()
        fetchLocationCoordinates()
    }

    private fun initialize() {
        auth = Firebase.auth
        binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        binding.drawerLayout.addDrawerListener(object : DrawerLayout.DrawerListener {
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
            }

            override fun onDrawerOpened(drawerView: View) {
                binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
            }

            override fun onDrawerClosed(drawerView: View) {
                binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
            }

            override fun onDrawerStateChanged(newState: Int) {
            }
        })
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireContext())
        val mapFragment = childFragmentManager.findFragmentById(R.id.maps) as SupportMapFragment

        mapFragment.getMapAsync {
            mMap = it
            mMap.uiSettings.isZoomGesturesEnabled = false
            mMap.uiSettings.isScrollGesturesEnabled = false
            mMap.uiSettings.isRotateGesturesEnabled = false
            mMap.uiSettings.isScrollGesturesEnabledDuringRotateOrZoom = false
            mMap.uiSettings.isTiltGesturesEnabled = false
            mMap.uiSettings.isCompassEnabled = false
            mMap.uiSettings.isMyLocationButtonEnabled = false
            mMap.setOnMarkerClickListener(this)
            findMyLocation()
        }
    }

    private fun saageg(status: LocationModel) {
//        if (mark == null) {
//            mark = mMap.addMarker(
//                MarkerOptions().title("LiveData").snippet("${status.latitude}, ${status.longitude}")
//                    .position(LatLng(status.latitude, status.longitude))
//            )!!
//        } else {
//            mark!!.snippet = "${status.latitude}, ${status.longitude}"
//            mark!!.position = LatLng(status.latitude, status.longitude)
//        }
    }

    private fun setupListener() = with(binding) {
        menu.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }
        navigationView.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.type_map -> {

                }
            }
            true
        }
    }

    private fun startDialog() {
        if (!dialogShowed) {
            dialogShowed = true
            val builder = AlertDialog.Builder(context, R.style.AlertDialogTheme)
            val view = layoutInflater.inflate(R.layout.permission_layout, null)

            val btnOk = view.findViewById<Button>(R.id.btn_ok)
            val btnCansel = view.findViewById<Button>(R.id.btn_cansel)
            val textView = view.findViewById<TextView>(R.id.text_permission_granted)

            builder.setView(view)
            val dialog = builder.create()

            dialog.setCancelable(false)

            btnCansel.setOnClickListener {

            }
            btnOk.setOnClickListener {
                if (ContextCompat.checkSelfPermission(
                        requireContext(), Manifest.permission.ACCESS_FINE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                        requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    dialog.dismiss()
                    checkGPS()
                } else {
                    dialogShowed = false
                    dialog.dismiss()
                    requestStoragePermission()
                }
            }
            if (dialog.window != null) {
                dialog.window?.setBackgroundDrawable(ColorDrawable(0))
            }
            dialog.show()
        } else {
            dialogShowed = true
        }
    }

    private fun isEnabledDontAskPermission(message: String) {
        val alertDialog: AlertDialog =
            AlertDialog.Builder(requireContext(), R.style.AlertDialogTheme)
                .setPositiveButton("Настройки", null)
                .setNegativeButton(getString(R.string.cansel_button), null).create()

        alertDialog.setCancelable(false)
        alertDialog.setMessage(message)

        alertDialog.setOnShowListener {
            val btnPositive: Button = (alertDialog).getButton(AlertDialog.BUTTON_POSITIVE)
            btnPositive.setOnClickListener {
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri: Uri = Uri.fromParts(
                    getString(R.string.packages), activity?.packageName, null
                )
                intent.data = uri
                startActivity(intent)
                alertDialog.dismiss()
                startDialog()
            }
            val btnNegative: Button = (alertDialog).getButton(AlertDialog.BUTTON_NEGATIVE)
            btnNegative.setOnClickListener {
                alertDialog.dismiss()
                updatePermissionCheckUI(PermissionStatus.Denied())
                startDialog()
            }
        }
        alertDialog.show()
    }

    private fun subscribeToGpsListener() =
        viewModel.gpsStatusLiveData(requireActivity()).observe(viewLifecycleOwner, gpsObserver)

    private fun subscribeToLocationPermissionListener() =
        viewModel.locationPermissionStatusLiveData(requireActivity())
            .observe(viewLifecycleOwner, permissionObserver)

    private fun subscribeToLocationLiveData() =
        viewModel.locationLiveData(requireActivity()).observe(viewLifecycleOwner, locationObserver)

    private fun checkGPS() {
        locationRequest = LocationRequest.Builder(5000)
            .setGranularity(Granularity.GRANULARITY_FINE)
            .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
            .setMinUpdateDistanceMeters(100F)
            .build()

        val builder = LocationSettingsRequest
            .Builder()
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
        markerOptions.title(currentLatLong.toString())
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

    private fun checkUserAccount() {
        db.collection("users").addSnapshotListener { documents, errors ->
            documents?.documents?.forEach {
                val doc = it.toObject(UsersModel::class.java)
                val user = auth?.currentUser
                if (user?.email.toString() == doc?.email) {
                    binding.nameUser.text = doc.name
                }
            }
        }
    }

    private fun findMyLocation() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(), Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mMap.isMyLocationEnabled = true
//            mMap.isMyLocationEnabled = true
//            fusedLocationProviderClient?.lastLocation?.addOnSuccessListener(requireActivity()) { location ->
//                if (location != null) {
//                    lastLocation = location
//                    val currentLatLong = LatLng(location.latitude, location.longitude)
//                    placeMarkerOnMap(currentLatLong)
//                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLong, 13f))
//                }
//            }
        } else {
            return
        }
    }

    private fun checkUserLocation() {
        val userEmail = auth?.currentUser?.email.toString()
        db.collection("coordinates").whereEqualTo("email", userEmail).get()
            .addOnSuccessListener { docEnabled ->
                if (docEnabled.size() == 0) {
                    addNewUserLocation()
                } else {
//                    val db = FirebaseFirestore.getInstance()
//                    val codeRef = db.collection("coordinates")
//                    codeRef.whereEqualTo("email", userEmail).get()
//                        .addOnCompleteListener { task ->
//                            if (task.isSuccessful) {
//                                for (document in task.result) {
//                                    val update: MutableMap<String, Any> = HashMap()
//                                    update["latitude"] = latitudeLiveData
//                                    update["longitude"] = longitudeLiveData
//                                    codeRef.document(document.id).set(update, SetOptions.merge())
//                                }
//                            }
//                        }
                }
            }
    }

    private fun addNewUserLocation() {
        val userEmail = auth?.currentUser?.email.toString()
        val coordinates = hashMapOf(
            "email" to userEmail, "latitude" to latitudeLiveData, "longitude" to longitudeLiveData
        )
//        viewModel.addLocation(latitudeLiveData, longitudeLiveData)
//        db.collection("coordinates").add(coordinates).addOnSuccessListener {
//            Toast.makeText(requireContext(), "Координаты добавлены", Toast.LENGTH_SHORT).show()
//        }
    }

    private fun fetchLocationCoordinates() {
        Log.e("coor", "rip")
        val userEmail = auth?.currentUser?.email.toString()
        db.collection("coordinates").get().addOnSuccessListener { value ->
            value?.documents?.forEach {
                Log.e("coor", "cycle")
                var mark: Marker? = null
                val doc = it.toObject(UserCoordinates::class.java)
//                if (mark) {
                val db = FirebaseFirestore.getInstance()
                val codeRef = db.collection("coordinates")
                codeRef.whereEqualTo("email", userEmail).get()
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            for (document in task.result) {
                                val update: MutableMap<String, Any> = HashMap()
                                update["markerEnabled"] = true
                                codeRef.document(document.id).set(update, SetOptions.merge())
                            }
                        }
                    }
                mark = mMap.addMarker(
                    MarkerOptions().title("LiveData").snippet("${doc?.latitude}, ${doc?.longitude}")
                        .position(LatLng(doc?.latitude!!, doc.longitude!!))
                )!!
//                } else {
                codeRef.whereEqualTo("email", userEmail).get()
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            for (document in task.result) {
                                val update: MutableMap<String, Any> = HashMap()
                                update["latitude"] = latitudeLiveData
                                update["longitude"] = longitudeLiveData
                                codeRef.document(document.id).set(update, SetOptions.merge())
                            }
                        }
                    }
                mark.snippet = "${doc.latitude}, ${doc.longitude}"
                mark.position = LatLng(doc.latitude!!, doc.longitude!!)
//                }
            }
        }
    }
}
