package com.miftah.jakasforpassenger.ui.maps

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.work.Constraints
import androidx.work.Data.Builder
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.google.maps.android.PolyUtil
import com.google.maps.model.DirectionsResult
import com.miftah.jakasforpassenger.R
import com.miftah.jakasforpassenger.core.services.LocationTrackerService
import com.miftah.jakasforpassenger.core.workers.FindRouteWorker
import com.miftah.jakasforpassenger.databinding.ActivityMapsBinding
import com.miftah.jakasforpassenger.utils.Constants.DESTINATION_LAT_LNG
import com.miftah.jakasforpassenger.utils.Constants.KEY_MAP
import com.miftah.jakasforpassenger.utils.Constants.MAP_ZOOM
import com.miftah.jakasforpassenger.utils.Constants.MapObjective
import com.miftah.jakasforpassenger.utils.Constants.POLYLINE_COLOR
import com.miftah.jakasforpassenger.utils.Constants.POLYLINE_WIDTH
import com.miftah.jakasforpassenger.utils.Constants.POSITION_LAT_LNG
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class MapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMapClickListener {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private val viewModel: MapsViewModel by viewModels()

    private lateinit var autoCompletePosition: AutocompleteSupportFragment
    private lateinit var autoCompleteDestination: AutocompleteSupportFragment

    private val latLngDestination: MutableMap<String, LatLng?> = mutableMapOf()
    private var polylineRoute: Polyline? = null

    @Inject
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    private var markerDestination: Marker? = null
    private var markerPosition: Marker? = null

    private lateinit var workManager: WorkManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        workManager = WorkManager.getInstance(this)
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.setOnMapClickListener(this)
        setupAutoComplete()
        findRoute()

        viewModel.pointDestination.observe(this) { data ->
            latLngDestination[MapObjective.DESTINATION.name] = data
            makeMarker(data, MapObjective.DESTINATION)
            findRoute()
        }

        viewModel.pointPosition.observe(this) { data ->
            latLngDestination[MapObjective.POSITION.name] = data
            makeMarker(data, MapObjective.POSITION)
            findRoute()
        }

        binding.btnFindPosition.setOnClickListener {
            fusedLocationProviderClient.lastLocation.addOnSuccessListener { data ->
                if (data != null) {
                    val latLng = LatLng(data.latitude, data.longitude)
                    viewModel.updatePoint(MapObjective.POSITION, latLng)
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, MAP_ZOOM))
                }else {
                    Timber.d("Empty")
                }
            }
        }
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

        autoCompleteDestination =
            supportFragmentManager.findFragmentById(binding.autocompleteDestinationFragment.id) as AutocompleteSupportFragment

        autoCompleteDestination.setPlaceFields(
            listOf(
                Place.Field.ID,
                Place.Field.LAT_LNG,
                Place.Field.ADDRESS
            )
        )

        autoCompleteDestination.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onError(status: Status) {
                Toast.makeText(this@MapsActivity, status.statusMessage, Toast.LENGTH_SHORT).show()
            }

            override fun onPlaceSelected(place: Place) {
                autoCompleteDestination.setText(place.address)
                val latLng = place.latLng
                latLng?.let { data ->
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(data, 12F))
                    viewModel.updatePoint(MapObjective.DESTINATION, data)
                }

            }

        })

        autoCompletePosition.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onError(status: Status) {
                Toast.makeText(this@MapsActivity, status.statusMessage, Toast.LENGTH_SHORT).show()
            }

            override fun onPlaceSelected(place: Place) {
                autoCompletePosition.setText(place.address)
                val latLng = place.latLng
                latLng?.let { data ->
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(data, 12F))
                    viewModel.updatePoint(MapObjective.POSITION, data)
                }
            }

        })
    }

    private var cursor = true
    override fun onMapClick(latLng: LatLng) {
        cursor = if (cursor) {
            viewModel.updatePoint(MapObjective.DESTINATION, latLng)
            false
        } else {
            viewModel.updatePoint(MapObjective.POSITION, latLng)
            true
        }
    }

    private fun makeMarker(latLng: LatLng?, markerName: MapObjective) {
        latLng ?: return
        when (markerName) {
            MapObjective.DESTINATION -> {
                markerPosition = if (markerPosition == null) {
                    mMap.addMarker(MarkerOptions().position(latLng).title(markerName.name))
                } else {
                    markerPosition?.remove()
                    mMap.addMarker(MarkerOptions().position(latLng).title(markerName.name))
                }
                Timber.d("onMapReady: ${latLngDestination[markerName.name]}")
            }

            MapObjective.POSITION -> {
                markerDestination = if (markerDestination == null) {
                    mMap.addMarker(MarkerOptions().position(latLng).title(markerName.name))
                } else {
                    markerDestination?.remove()
                    mMap.addMarker(MarkerOptions().position(latLng).title(markerName.name))
                }
                Timber.d("onMapReady: ${latLngDestination[markerName.name]}")
            }
        }
    }

    private fun findRoute() {

        if (latLngDestination[MapObjective.POSITION.name] == null || latLngDestination[MapObjective.DESTINATION.name] == null) {
            return
        }

        val positionLatLng =
            "${latLngDestination[MapObjective.POSITION.name]?.latitude},${latLngDestination[MapObjective.POSITION.name]?.longitude}"
        val destinationLatLng =
            "${latLngDestination[MapObjective.DESTINATION.name]?.latitude},${latLngDestination[MapObjective.DESTINATION.name]?.longitude}"

        val data = Builder()
            .putString(POSITION_LAT_LNG, positionLatLng)
            .putString(DESTINATION_LAT_LNG, destinationLatLng)
            .build()

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val workRequest = OneTimeWorkRequestBuilder<FindRouteWorker>()
            .setInputData(data)
            .setConstraints(constraints)
            .build()

        workManager.enqueue(workRequest)
        workManager.getWorkInfoByIdLiveData(workRequest.id)
            .observe(this@MapsActivity) { workInfo ->
                if (WorkInfo.State.ENQUEUED == workInfo.state) {
                    binding.progressBar.visibility = View.VISIBLE
                }
                if (WorkInfo.State.SUCCEEDED == workInfo.state) {
                    binding.progressBar.visibility = View.GONE
                    FindRouteWorker.workRouteResult?.let {
                        drawRoute(it)
                    }
                    Timber.d("success")
                }
                if (WorkInfo.State.FAILED == workInfo.state) {
                    binding.progressBar.visibility = View.GONE
                    Timber.d("Failed")
                }
            }
    }

    private fun drawRoute(directionsResult: DirectionsResult) {
        val decodedPath = PolyUtil.decode(directionsResult.routes[0].overviewPolyline.encodedPath)

        val polylineOptions = PolylineOptions().addAll(decodedPath)
            .width(POLYLINE_WIDTH)
            .color(POLYLINE_COLOR)

        polylineRoute?.remove()

        polylineRoute = mMap.addPolyline(polylineOptions)

        val bounds = LatLngBounds.builder()
        for (point in decodedPath) {
            bounds.include(point)
        }

        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds.build(), 50))
    }

    private fun sendCommandToService(action: String) =
        Intent(this, LocationTrackerService::class.java).let {
            it.action = action
            startService(it)
        }


}