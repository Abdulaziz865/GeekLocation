package com.example.presentation.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.presentation.R
import com.example.presentation.databinding.FragmentGoogleMapBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class GoogleMapFragment : Fragment(), OnMapReadyCallback {
    private lateinit var binding: FragmentGoogleMapBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGoogleMapBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapFragment = childFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync {
            val karabalta = LatLng(42.82112116502111, 73.84416404971903)
            it.addMarker(MarkerOptions().position(karabalta).title("Кара-Балта"))
            it.moveCamera(CameraUpdateFactory.newLatLngZoom(karabalta, 13f))
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {

    }
}
