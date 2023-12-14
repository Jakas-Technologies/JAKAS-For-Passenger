package com.miftah.jakasforpassenger.core.services

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.os.Looper
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import com.miftah.jakasforpassenger.R
import com.miftah.jakasforpassenger.core.data.source.remote.dto.response.GeoGamma
import com.miftah.jakasforpassenger.core.data.source.remote.socket.SocketHandlerService
import com.miftah.jakasforpassenger.ui.maps.MapsActivity
import com.miftah.jakasforpassenger.utils.Angkot
import com.miftah.jakasforpassenger.utils.Constants
import com.miftah.jakasforpassenger.utils.Constants.ACTION_START_SERVICE
import com.miftah.jakasforpassenger.utils.Constants.ACTION_STOP_SERVICE
import com.miftah.jakasforpassenger.utils.Constants.EXTRA_DEPARTMENT_ANGKOT
import com.miftah.jakasforpassenger.utils.Constants.EXTRA_DESTINATION_SERIALIZABLE
import com.miftah.jakasforpassenger.utils.Constants.EXTRA_POSITION_SERIALIZABLE
import com.miftah.jakasforpassenger.utils.Constants.NOTIFICATION_CHANNEL_ID
import com.miftah.jakasforpassenger.utils.Constants.NOTIFICATION_ID
import com.miftah.jakasforpassenger.utils.MapsUtility
import com.miftah.jakasforpassenger.utils.SerializableLatLng
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class LocationTrackerService : LifecycleService() {

    private var serviceKilled = false

    @Inject
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    @Inject
    lateinit var socketHandlerService: SocketHandlerService

    private var destinationPath: SerializableLatLng? = null
    private var positionPath: SerializableLatLng? = null
    private var angkotDepartment: Angkot? = null

    companion object {
        val userPosition = MutableLiveData<LatLng>()
        val destinationPosition = MutableLiveData<LatLng>()
        val realtimeUserPosition = MutableLiveData<LatLng>()
        val angkotChoice = MutableLiveData<Angkot>()
        val angkotPosition = MutableLiveData<List<LatLng>>()
        val isTracking = MutableLiveData<Boolean>()
    }

    override fun onCreate() {
        super.onCreate()
        postInitialValues()

        isTracking.observe(this) {
            updateLocationTracking(it)
        }
    }

    @Suppress("DEPRECATION")
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            when (it.action) {
                ACTION_START_SERVICE -> {
                    if (Build.VERSION.SDK_INT >= 33) {
                        destinationPath = intent.getParcelableExtra(
                            EXTRA_DESTINATION_SERIALIZABLE,
                            SerializableLatLng::class.java
                        )
                        positionPath = intent.getParcelableExtra(
                            EXTRA_POSITION_SERIALIZABLE,
                            SerializableLatLng::class.java
                        )
                        angkotDepartment = intent.getParcelableExtra(
                            EXTRA_DEPARTMENT_ANGKOT,
                            Angkot::class.java
                        )
                    } else {
                        destinationPath = intent.getParcelableExtra(EXTRA_DESTINATION_SERIALIZABLE)
                        positionPath = intent.getParcelableExtra(EXTRA_POSITION_SERIALIZABLE)
                        angkotDepartment = intent.getParcelableExtra(EXTRA_DEPARTMENT_ANGKOT)
                    }
                    postInitialValues()
                    initTracking()
                    startForegroundService()
                    Timber.d("Start Service")
                }

                ACTION_STOP_SERVICE -> {
                    Timber.d("Stop Service")
                    killService()
                }
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun killService() {
        serviceKilled = true
        socketHandlerService.closeConnection()
        postInitialValues()
        stopForeground(true)
        stopSelf()
    }

    private fun postInitialValues() {
        isTracking.postValue(false)
        userPosition.postValue(LatLng(0.0, 0.0))
        destinationPosition.postValue(LatLng(0.0, 0.0))
        realtimeUserPosition.postValue(LatLng(0.0, 0.0))
        angkotPosition.postValue(mutableListOf())
    }

    private fun initTracking() {
        destinationPath?.let {
            destinationPosition.postValue(LatLng(it.latitude, it.longitude))
        }
        positionPath?.let {
            userPosition.postValue(LatLng(it.latitude, it.longitude))
        }
        angkotDepartment?.let {
            angkotChoice.postValue(it)
            socketHandlerService.initSession(it)
        }
    }

    @SuppressLint("MissingPermission")
    private fun updateLocationTracking(isTracking: Boolean) {
        if (!MapsUtility.hasLocationPermissions(this)) return
        if (isTracking) {
            val request = LocationRequest.create().apply {
                interval = Constants.LOCATION_UPDATE_INTERVAL
                fastestInterval = Constants.FASTEST_LOCATION_INTERVAL
                priority = Priority.PRIORITY_HIGH_ACCURACY
            }
            fusedLocationProviderClient.requestLocationUpdates(
                request,
                locationCallback,
                Looper.getMainLooper()
            )
        } else {
            fusedLocationProviderClient.removeLocationUpdates(locationCallback)
        }
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(result: LocationResult) {
            super.onLocationResult(result)
            result.locations.let { locations ->
                for (location in locations) {
                    val lastLatLng = LatLng(
                        location.latitude,
                        location.longitude
                    )
                    realtimeUserPosition.postValue(lastLatLng)
                    socketHandlerService.sendUserPosition(lastLatLng)
/*                    socketHandlerService.getAngkotPosition {
                        Timber.d("what")
                    }*/
                    Timber.d("NEW LOCATION: ${location.latitude}, ${location.longitude}")
                }
            }
        }
    }

    private fun startForegroundService() {
        socketHandlerService.establishConnection()
        isTracking.postValue(true)
        val notificationBuilder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setAutoCancel(false)
            .setOngoing(true)
            .setSmallIcon(R.drawable.iconlocation2)
            .setContentTitle("Find Transportation Near You")
            .setContentIntent(pendingIntent())

        startForeground(NOTIFICATION_ID, notificationBuilder.build())
    }

    private fun getAllAngkotDirection() {
        socketHandlerService.getAngkotPosition { jsonDataList ->
            jsonDataList.forEach { jsonData ->
                val geoJson = Gson().fromJson(jsonData, GeoGamma::class.java)
            }
        }
    }

    private fun pendingIntent(): PendingIntent {
        val resultIntent = Intent(this, MapsActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        }
        return PendingIntent.getActivity(
            this,
            0,
            resultIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
    }
}