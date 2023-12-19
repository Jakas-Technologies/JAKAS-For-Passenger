package com.miftah.jakasforpassenger.ui.maps

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.work.Constraints
import androidx.work.Data.Builder
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.PlaceTypes
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FetchPlaceResponse
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_COLLAPSED
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_EXPANDED
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_HALF_EXPANDED
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_HIDDEN
import com.google.maps.android.PolyUtil
import com.google.maps.model.DirectionsResult
import com.miftah.jakasforpassenger.R
import com.miftah.jakasforpassenger.core.services.LocationTrackerService
import com.miftah.jakasforpassenger.core.workers.FindRouteWorker
import com.miftah.jakasforpassenger.databinding.ActivityMapsBinding
import com.miftah.jakasforpassenger.ui.qrscanner.QrCodeScannerActivity
import com.miftah.jakasforpassenger.ui.transaction.TransactionActivity
import com.miftah.jakasforpassenger.utils.Angkot
import com.miftah.jakasforpassenger.utils.Constants
import com.miftah.jakasforpassenger.utils.Constants.ACTION_CANCEL_PAYING_SERVICE
import com.miftah.jakasforpassenger.utils.Constants.ACTION_START_SERVICE
import com.miftah.jakasforpassenger.utils.Constants.ACTION_STOP_SERVICE
import com.miftah.jakasforpassenger.utils.Constants.DESTINATION_LAT_LNG
import com.miftah.jakasforpassenger.utils.Constants.EXTRA_DEPARTMENT_ANGKOT
import com.miftah.jakasforpassenger.utils.Constants.EXTRA_DESTINATION_SERIALIZABLE
import com.miftah.jakasforpassenger.utils.Constants.EXTRA_IDENTITY_ANGKOT
import com.miftah.jakasforpassenger.utils.Constants.EXTRA_POSITION_SERIALIZABLE
import com.miftah.jakasforpassenger.utils.Constants.EXTRA_QR_CODE
import com.miftah.jakasforpassenger.utils.Constants.KEY_MAP
import com.miftah.jakasforpassenger.utils.Constants.MAP_ZOOM
import com.miftah.jakasforpassenger.utils.Constants.MapObjective
import com.miftah.jakasforpassenger.utils.Constants.POLYLINE_COLOR
import com.miftah.jakasforpassenger.utils.Constants.POLYLINE_WIDTH
import com.miftah.jakasforpassenger.utils.Constants.POSITION_LAT_LNG
import com.miftah.jakasforpassenger.utils.QrScanning
import com.miftah.jakasforpassenger.utils.Result
import com.miftah.jakasforpassenger.utils.SerializableDestination
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class MapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMapClickListener,
    View.OnFocusChangeListener, TextWatcher {

    private lateinit var includeSearchbar: CardView
    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private val viewModel: MapsViewModel by viewModels()

    private val nameDestination: MutableMap<String, SerializableDestination?> = mutableMapOf()
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var placesClient: PlacesClient
    private var sessionToken: AutocompleteSessionToken? = null

    private val adapter = PlacePredictionAdapter()

    private var angkotChoice: Angkot? = null
    private var polylineRoute: Polyline? = null
    private var userPosition: LatLng = LatLng(0.0, 0.0)
    private var angkotIdentity: QrScanning? = null

    private var serviceOn = false
    private var isFilled = false

    private lateinit var autocompleteBottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
    private lateinit var paymentBottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>

    @Inject
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    private var markerDestination: Marker? = null
    private var markerPosition: Marker? = null
    private var markerUser: Marker? = null

    private lateinit var workManager: WorkManager

    private val resultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK && result.data != null) {
            angkotIdentity = if (Build.VERSION.SDK_INT >= 33) {
                result.data?.getParcelableExtra(EXTRA_QR_CODE, QrScanning::class.java)
            } else {
                result.data?.getParcelableExtra(EXTRA_QR_CODE)
            }
            angkotIdentity?.let {
                sendCommandToService(Constants.ACTION_START_PAYING_SERVICE)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        Places.initialize(applicationContext, KEY_MAP)

        workManager = WorkManager.getInstance(this)
        sessionToken = AutocompleteSessionToken.newInstance()
        placesClient = Places.createClient(this)

        includeSearchbar = findViewById(R.id.search_position_inc)

        autocompleteBottomSheetBehavior =
            BottomSheetBehavior.from(findViewById(R.id.autocomplete_inc))
        paymentBottomSheetBehavior = BottomSheetBehavior.from(findViewById(R.id.payment_state_inc))

        paymentBottomSheetBehavior.state = STATE_HIDDEN
        autocompleteBottomSheetBehavior.halfExpandedRatio = 0.5f
        autocompleteBottomSheetBehavior.state = STATE_HALF_EXPANDED

        autocompleteBehaviour()
        searchbarBehaviour()
        subscribeToObservers()
        autocompleteRv()
    }

    private fun searchbarBehaviour() {
        binding.btnToSearchAct.setOnClickListener {
            autocompleteBottomSheetBehavior.state = STATE_HALF_EXPANDED
        }
    }

    private fun subscribeToObservers() {
        LocationTrackerService.isTracking.observe(this) { isTracking ->
            viewModel.updateServiceStatus(isTracking)
        }
        LocationTrackerService.angkotPosition.observe(this) { allAngkotPosition ->
//            if (serviceOn) {
//
//            }
        }
        LocationTrackerService.userPosition.observe(this) { userLastPosition ->
            if (serviceOn) {
                viewModel.updateUserPosition(userLastPosition)
                polylineRoute?.let {
                    viewModel.isUserOnPath(userLastPosition, it)
                }
                val camera = CameraPosition.builder()
                    .target(userLastPosition)
                    .zoom(MAP_ZOOM)
                    .build()
                if (!serviceOn) mMap.animateCamera(
                    CameraUpdateFactory.newCameraPosition(camera),
                    100,
                    null
                )
            }
        }
        LocationTrackerService.realtimeUserPosition.observe(this) { realtimePosition ->
            if (serviceOn) {
                viewModel.updateUserPosition(realtimePosition)
                polylineRoute?.let {
                    viewModel.isUserOnPath(realtimePosition, it)
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.setOnMapClickListener(this)
        findRoute()

        viewModel.pointDestination.observe(this) { data ->
            data?.let {
                nameDestination[MapObjective.DESTINATION.name] = data
                makeMarker(data.latLng, MapObjective.DESTINATION)
                binding.autocompleteInc.edInputDestination.setText(data.name)
                binding.tvUserDestination.text = data.name
            }
        }

        viewModel.pointPosition.observe(this) { data ->
            data?.let {
                nameDestination[MapObjective.POSITION.name] = data
                makeMarker(data.latLng, MapObjective.POSITION)
                binding.autocompleteInc.edInputPosition.setText(data.name)
                binding.tvUserPosition.text = data.name
            }
        }

        viewModel.isPointFilled.observe(this) { isFilled ->
            if (isFilled) {
                this.isFilled = isFilled
                angkotDirectionRv()
                paymentBehaviour()
                findRoute()
            }
        }

        viewModel.isOnPath.observe(this) { onPath ->
            if (!onPath) {
                findRoute()
            }
        }

        viewModel.userPosition.observe(this) { data ->
            markerUser?.remove()
            markerUser = mMap.addMarker(MarkerOptions().position(data).title("USER"))
            val camera = CameraPosition.builder()
                .target(data)
                .zoom(MAP_ZOOM)
                .build()
            if (!serviceOn) mMap.animateCamera(
                CameraUpdateFactory.newCameraPosition(camera),
                100,
                null
            )
        }

        binding.fabFindMyLocation.setOnClickListener {
            fusedLocationProviderClient.lastLocation.addOnSuccessListener { data ->
                if (data != null) {
                    val latLng = LatLng(data.latitude, data.longitude)
                    /*val namePosition = "Your Position"
                    val serializableDestination = SerializableDestination(
                        name = namePosition,
                        address = namePosition,
                        latLng = latLng
                    )
                    viewModel.updatePoint(MapObjective.POSITION, serializableDestination)*/
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, MAP_ZOOM))
                } else {
                    Timber.d("Empty")
                }
            }
        }

        viewModel.serviceLive.observe(this) {
            this.serviceOn = it
            paymentBehaviour()
        }
    }

    private fun angkotDirectionRv() {
        val linearLayoutManager = LinearLayoutManager(this)
        binding.paymentStateInc.rvAngkotDirection.layoutManager = linearLayoutManager
        viewModel.findAngkotBaseOnPositionAndDestination(
            nameDestination[MapObjective.POSITION.name] as SerializableDestination,
            nameDestination[MapObjective.DESTINATION.name] as SerializableDestination
        ).observe(this) { result ->
            val adapter = AngkotDepartmentAdapter(
                onClick = { angkot ->
                    binding.paymentStateInc.paymentHasSelectedContainer.visibility = View.VISIBLE
                    angkotChoice = angkot
                    binding.paymentStateInc.priceHasSelected.text = angkot.price.toString()
                    binding.paymentStateInc.angkotDirection.text = angkot.id.toString()
                }
            )
            when (result) {
                is Result.Success -> {
                    binding.progressBar.visibility = View.GONE
                    adapter.submitList(result.data)
                    binding.paymentStateInc.rvAngkotDirection.adapter = adapter
                }

                is Result.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }

                is Result.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(this, result.error, Toast.LENGTH_SHORT).show()
                }

                else -> {}
            }
        }
    }

    private fun autocompleteRv() {
        val layoutManager = LinearLayoutManager(this)
        binding.autocompleteInc.apply {
            rvSearchItem.layoutManager = layoutManager
            rvSearchItem.adapter = adapter
            rvSearchItem.addItemDecoration(
                DividerItemDecoration(
                    this@MapsActivity,
                    layoutManager.orientation
                )
            )
        }
        adapter.onPlaceClickListener = {
            CoroutineScope(Dispatchers.Main).launch {
                findPlaceByAutocompletePrediction(it)
            }
        }
    }

    private fun findPlaceByAutocompletePrediction(placePrediction: AutocompletePrediction) {
        val placeFields = listOf(Place.Field.LAT_LNG, Place.Field.NAME, Place.Field.ADDRESS)
        val request = FetchPlaceRequest.newInstance(placePrediction.placeId, placeFields)

        placesClient.fetchPlace(request)
            .addOnSuccessListener { response: FetchPlaceResponse ->
                val place = response.place
                place.latLng ?: return@addOnSuccessListener
                val serializableDestination = SerializableDestination(
                    name = place.name,
                    address = place.address,
                    latLng = place.latLng!!
                )
                if (searchbarFocus) {
                    viewModel.updatePoint(MapObjective.POSITION, serializableDestination)
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(place.latLng!!, MAP_ZOOM))
                } else {
                    viewModel.updatePoint(MapObjective.DESTINATION, serializableDestination)
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(place.latLng!!, MAP_ZOOM))
                }
            }.addOnFailureListener { exception: Exception ->
                if (exception is ApiException) {
                    Timber.e("Place not found: ${exception.message} ${exception.statusCode}")
                }
            }
    }

    private fun autocompleteBehaviour() {
        binding.autocompleteInc.apply {
            edInputPosition.onFocusChangeListener = this@MapsActivity
            edInputDestination.onFocusChangeListener = this@MapsActivity
            edInputPosition.addTextChangedListener(this@MapsActivity)
            edInputDestination.addTextChangedListener(this@MapsActivity)
        }
        autocompleteBottomSheetBehavior.addBottomSheetCallback(object : BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    STATE_EXPANDED -> {
                        includeSearchbar.visibility = View.GONE
                        binding.fabFindMyLocation.visibility = View.GONE
                    }

                    STATE_COLLAPSED, STATE_HIDDEN -> {
                        includeSearchbar.visibility = View.VISIBLE
                        binding.fabFindMyLocation.visibility = View.VISIBLE
                    }

                    else -> {}
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })
    }

    private fun paymentBehaviour() {
        if (!isFilled) return
        autocompleteBottomSheetBehavior.state = STATE_HIDDEN
        paymentBottomSheetBehavior.state = STATE_EXPANDED
        binding.paymentStateInc.apply {
            if (!serviceOn) {
                btnConfirmFind.visibility = View.VISIBLE
                rvAngkotDirection.visibility = View.VISIBLE
                paymentHasSelectedContainer.visibility = View.GONE
                btnScanFind.visibility = View.GONE
                btnCancelFind.visibility = View.GONE
                btnFinishPayment.visibility = View.GONE
                btnCancelPayment.visibility = View.GONE
            } else {
                if (angkotIdentity == null) {
                    btnConfirmFind.visibility = View.GONE
                    rvAngkotDirection.visibility = View.GONE
                    btnScanFind.visibility = View.VISIBLE
                    btnCancelFind.visibility = View.VISIBLE
                    btnFinishPayment.visibility = View.GONE
                    btnCancelPayment.visibility = View.GONE
                } else {
                    btnFinishPayment.visibility = View.VISIBLE
                    btnCancelPayment.visibility = View.VISIBLE
                    btnConfirmFind.visibility = View.GONE
                    rvAngkotDirection.visibility = View.GONE
                    btnScanFind.visibility = View.GONE
                    btnCancelFind.visibility = View.GONE
                }
            }

            btnConfirmFind.setOnClickListener {
                sendCommandToService(ACTION_START_SERVICE)
            }

            btnCancelFind.setOnClickListener {
                sendCommandToService(ACTION_STOP_SERVICE)
            }

            btnScanFind.setOnClickListener {
//                angkotIdentity = QrRequest(1, 1, 1.1)
//                sendCommandToService(ACTION_STOP_SERVICE)
                Intent(this@MapsActivity, QrCodeScannerActivity::class.java).apply {
                    resultLauncher.launch(this)
                }
            }

            btnCancelPayment.setOnClickListener {
                angkotIdentity = null
                sendCommandToService(ACTION_CANCEL_PAYING_SERVICE)
            }

            btnFinishPayment.setOnClickListener {
                angkotIdentity = null
                sendCommandToService(ACTION_STOP_SERVICE)
                Intent(this@MapsActivity, TransactionActivity::class.java).let {
                    it.putExtra(Constants.EXTRA_DRIVER_ID, angkotChoice?.id.toString())
                    it.putExtra(Constants.EXTRA_AMOUNT, 1)
                    it.putExtra(Constants.EXTRA_PRICE, angkotChoice?.price as Int)
                    startActivity(it)
                }
            }
        }

    }

    private var cursor = true
    override fun onMapClick(latLng: LatLng) {
        val nameDestination = "Your Destination"
        val namePosition = "Your Position"
        cursor = if (cursor) {
            viewModel.updatePoint(
                MapObjective.POSITION, SerializableDestination(
                    name = namePosition,
                    address = namePosition,
                    latLng = latLng
                )
            )
            false
        } else {
            viewModel.updatePoint(
                MapObjective.DESTINATION, SerializableDestination(
                    name = nameDestination,
                    address = nameDestination,
                    latLng = latLng
                )
            )
            true
        }
    }

    private fun makeMarker(latLng: LatLng?, markerName: MapObjective) {
        latLng ?: return
        when (markerName) {
            MapObjective.DESTINATION -> {
                markerPosition?.remove()
                markerPosition = mMap.addMarker(
                    MarkerOptions()
                        .position(latLng)
                        .title(markerName.name)
                )
                Timber.d("onMapReady: ${nameDestination[markerName.name]}")
            }

            MapObjective.POSITION -> {
                markerDestination?.remove()
//                val newIcon = BitmapDescriptorFactory.fromResource(R.drawable.ic_user_location)
                markerDestination =
                    mMap.addMarker(
                        MarkerOptions()
                            .position(latLng)
                            .title(markerName.name)
                    )
                Timber.d("onMapReady: ${nameDestination[markerName.name]}")
            }
        }
    }

    private fun findRoute() {
        if (nameDestination[MapObjective.POSITION.name] == null || nameDestination[MapObjective.DESTINATION.name] == null) {
            return
        }

        val positionLatLng =
            "${nameDestination[MapObjective.POSITION.name]?.latLng?.latitude},${nameDestination[MapObjective.POSITION.name]?.latLng?.longitude}"
        val destinationLatLng =
            "${nameDestination[MapObjective.DESTINATION.name]?.latLng?.latitude},${nameDestination[MapObjective.DESTINATION.name]?.latLng?.longitude}"

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
                when (workInfo.state) {
                    WorkInfo.State.ENQUEUED -> binding.progressBar.visibility = View.VISIBLE
                    WorkInfo.State.RUNNING -> Timber.d("Work RUNNING")
                    WorkInfo.State.SUCCEEDED -> {
                        binding.progressBar.visibility = View.GONE
                        FindRouteWorker.workRouteResult?.let {
                            drawRoute(it)
                        }
                        Timber.d("success")
                    }

                    WorkInfo.State.FAILED -> {
                        binding.progressBar.visibility = View.GONE
                        Timber.d("Work FAILED")
                    }

                    WorkInfo.State.BLOCKED -> Timber.d("Work BLOCKED")
                    WorkInfo.State.CANCELLED -> Timber.d("Work CANCELLED")
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
        if (!serviceOn) mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds.build(), 50))
    }

    private var searchbarFocus = true

    override fun onFocusChange(v: View?, hasFocus: Boolean) {
        when (v?.id) {
            R.id.ed_input_position -> {
                searchbarFocus = hasFocus
            }

            R.id.ed_input_destination -> {
                searchbarFocus = !hasFocus
            }
        }
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

    override fun afterTextChanged(s: Editable?) {
        binding.progressBar.isIndeterminate = true
        handler.removeCallbacksAndMessages(null)
        handler.postDelayed({ getPlacePredictions(s.toString()) }, 300)
    }

    private fun getPlacePredictions(query: String) {
        val newRequest = FindAutocompletePredictionsRequest.builder()
            .setCountries("ID")
            .setTypesFilter(listOf(PlaceTypes.ESTABLISHMENT))
            .setSessionToken(sessionToken)
            .setQuery(query)
            .build()

        placesClient.findAutocompletePredictions(newRequest)
            .addOnSuccessListener { response ->
                val predictions = response.autocompletePredictions
                adapter.setPredictions(predictions)
                binding.progressBar.isIndeterminate = false
            }.addOnFailureListener { exception: Exception? ->
                binding.progressBar.isIndeterminate = false
                if (exception is ApiException) {
                    Timber.e(exception)
                }
            }
    }

    private fun sendCommandToService(action: String) {
        if (nameDestination[MapObjective.POSITION.name] == null || nameDestination[MapObjective.DESTINATION.name] == null || angkotChoice == null) {
            Timber.e("Something become wrong")
            return
        }

        Intent(this, LocationTrackerService::class.java).let {
            it.action = action
            it.putExtra(EXTRA_POSITION_SERIALIZABLE, nameDestination[MapObjective.POSITION.name])
            it.putExtra(
                EXTRA_DESTINATION_SERIALIZABLE,
                nameDestination[MapObjective.DESTINATION.name]
            )
            it.putExtra(EXTRA_DEPARTMENT_ANGKOT, angkotChoice as Angkot)
            angkotIdentity?.let { data ->
                it.putExtra(EXTRA_IDENTITY_ANGKOT, data)
            }
            startService(it)
        }
    }

}