package com.miftah.jakasforpassenger.ui.maps

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.common.api.Status
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.miftah.jakasforpassenger.R
import com.miftah.jakasforpassenger.databinding.ActivityMapsBinding
import com.miftah.jakasforpassenger.utils.Constants.KEY_MAP
import com.miftah.jakasforpassenger.utils.MapObjective
import timber.log.Timber

class MapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMapClickListener {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private val viewModel: MapsViewModel by viewModels()

    private lateinit var autoCompletePosition: AutocompleteSupportFragment
    private lateinit var autoCompleteDestination: AutocompleteSupportFragment

    private val latLngDestination: MutableMap<String, LatLng?> = mutableMapOf()

    private var markerDestination: Marker? = null
    private var markerPosition: Marker? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.setOnMapClickListener(this)
        setupAutoComplete()
    }

    private fun setupAutoComplete() {
        Places.initialize(applicationContext, KEY_MAP)
        autoCompletePosition =
            supportFragmentManager.findFragmentById(binding.autocompletePositionFragment.id) as AutocompleteSupportFragment

        autoCompletePosition.setPlaceFields(
            listOf(
                Place.Field.ID,
                Place.Field.LAT_LNG,
                Place.Field.ADDRESS
            )
        )

        autoCompletePosition.setHint("Input Posisi")

        autoCompleteDestination =
            supportFragmentManager.findFragmentById(binding.autocompleteDestinationFragment.id) as AutocompleteSupportFragment

        autoCompleteDestination.setPlaceFields(
            listOf(
                Place.Field.ID,
                Place.Field.LAT_LNG,
                Place.Field.ADDRESS
            )
        )

        autoCompleteDestination.setHint("Input Destinasi")

        autoCompleteDestination.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onError(status: Status) {
                Toast.makeText(this@MapsActivity, status.statusMessage, Toast.LENGTH_SHORT).show()
            }

            override fun onPlaceSelected(place: Place) {
                val latLng = place.latLng
                latLng?.let { data ->
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(data, 12F))
                    latLngDestination[MapObjective.DESTINATION.name] = data
                    makeMarker(data, MapObjective.DESTINATION)
                }

            }

        })

        autoCompletePosition.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onError(status: Status) {
                Toast.makeText(this@MapsActivity, status.statusMessage, Toast.LENGTH_SHORT).show()
            }

            override fun onPlaceSelected(place: Place) {
                val latLng = place.latLng
                latLng?.let { data ->
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(data, 12F))
                    latLngDestination[MapObjective.POSITION.name] = data
                    makeMarker(data, MapObjective.POSITION)
                }
            }

        })
    }

    private var cursor = true
    override fun onMapClick(latLng: LatLng) {
        cursor = if (cursor) {
            makeMarker(latLng, MapObjective.DESTINATION)
            false
        } else {
            makeMarker(latLng, MapObjective.POSITION)
            true
        }
    }

    private fun makeMarker(latLng: LatLng, markerName: MapObjective) {
        when (markerName) {
            MapObjective.DESTINATION -> {
                markerPosition = if (markerPosition == null) {
                    latLngDestination[markerName.name] = latLng
                    mMap.addMarker(MarkerOptions().position(latLng).title(markerName.name))
                } else {
                    latLngDestination.replace(markerName.name, latLng)
                    markerPosition?.remove()
                    mMap.addMarker(MarkerOptions().position(latLng).title(markerName.name))
                }
                Timber.d("onMapReady: ${latLngDestination[markerName.name]}")
            }

            MapObjective.POSITION -> {
                markerDestination = if (markerDestination == null) {
                    latLngDestination[markerName.name] = latLng
                    mMap.addMarker(MarkerOptions().position(latLng).title(markerName.name))
                } else {
                    latLngDestination.replace(markerName.name, latLng)
                    markerDestination?.remove()
                    mMap.addMarker(MarkerOptions().position(latLng).title(markerName.name))
                }
                Timber.d("onMapReady: ${latLngDestination[markerName.name]}")
            }
        }
    }
}