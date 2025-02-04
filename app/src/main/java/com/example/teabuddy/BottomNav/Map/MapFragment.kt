package com.example.teabuddy.BottomNav.Map

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Camera
import android.graphics.Canvas
import android.location.Location
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.FragmentManager
import com.example.teabuddy.R
import com.example.teabuddy.databinding.FragmentMapBinding
import com.example.teabuddy.databinding.FragmentTimerBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.model.Place


class MapFragment : Fragment(), OnMapReadyCallback {
    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var mMap: GoogleMap? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding =FragmentMapBinding.inflate(inflater, container, false)

        val mapFragment = childFragmentManager.findFragmentById(R.id.MapFragmentLayout) as? SupportMapFragment
        mapFragment?.getMapAsync(this)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        binding.buttonZoom.setOnClickListener{ mMap?.animateCamera(CameraUpdateFactory.zoomIn())}
        binding.buttonOut.setOnClickListener{ mMap?.animateCamera(CameraUpdateFactory.zoomOut())}

        return binding.root
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap?.setInfoWindowAdapter(MarkerInfoWindowAdapter(requireContext()))
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap?.isMyLocationEnabled = true
            moveToCurrentLocation()
        } else {
            Toast.makeText(requireContext(), "Нема доступу до геолокації", Toast.LENGTH_SHORT).show()
        }


        val startLocation = LatLng(49.408936953852674, 26.96041616644034)
        var newmarker = createPoint(startLocation, "ХНУ")
        var test = newmarker?.tag.toString()

        mMap?.isBuildingsEnabled = true
        mMap?.moveCamera(CameraUpdateFactory.newLatLng(startLocation))
        mMap?.uiSettings?.isMyLocationButtonEnabled = true
        mMap?.uiSettings?.isZoomControlsEnabled = false

        mMap?.setOnMarkerClickListener {marker ->
            marker.showInfoWindow()
            true
        }

    }
    private fun createPoint(location: LatLng, title: String): Marker? {
        val icon = BitmapDescriptorFactory.fromBitmap(getBitmapDrawable(R.drawable.tealeaf))
        val marker = mMap?.addMarker(
            MarkerOptions()
                .position(location)
                .icon(icon)
                .title(title)
        )
        marker?.tag = TeahouseModel(title, "Інститутська", 4.5f)
        return marker
    }

    private fun getBitmapDrawable(resid: Int): Bitmap {
        val drawable = ResourcesCompat.getDrawable(resources, resid, null)
        if (drawable != null) {
            val bitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            drawable.setBounds(0, 0, canvas.width, canvas.height)
            drawable.draw(canvas)
            return bitmap
        }
        return Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888)
    }

    private fun moveToCurrentLocation() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                location?.let {
                    val currentLatLng = LatLng(it.latitude, it.longitude)
                    mMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15f))
                }
            }
        }
    }
}