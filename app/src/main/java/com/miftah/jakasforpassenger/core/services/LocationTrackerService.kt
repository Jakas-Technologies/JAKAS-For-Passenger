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
import com.google.maps.model.LatLng
import com.miftah.jakasforpassenger.R
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

    private var destinationPath : SerializableLatLng? = null
    private var positionPath : SerializableLatLng? = null
    private var angkotDepartment : Angkot? = null

    companion object {
        val userPosition = MutableLiveData<LatLng>()
        val destinationPosition = MutableLiveData<LatLng>()
        val pathDirection = MutableLiveData<List<LatLng>>()
        val angkotPosition = MutableLiveData<List<LatLng>>()
        val isTracking = MutableLiveData<Boolean>()
    }

    @Suppress("DEPRECATION")
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            when (it.action) {
                ACTION_START_SERVICE -> {
                    if (Build.VERSION.SDK_INT >= 33) {
                        destinationPath = intent.getParcelableExtra(EXTRA_POSITION_SERIALIZABLE, SerializableLatLng::class.java)
                        positionPath = intent.getParcelableExtra(EXTRA_DESTINATION_SERIALIZABLE, SerializableLatLng::class.java)
                        angkotDepartment = intent.getParcelableExtra(EXTRA_DEPARTMENT_ANGKOT, Angkot::class.java)
                    } else {
                        destinationPath = intent.getParcelableExtra(EXTRA_POSITION_SERIALIZABLE)
                        positionPath = intent.getParcelableExtra(EXTRA_DESTINATION_SERIALIZABLE)
                        angkotDepartment = intent.getParcelableExtra(EXTRA_DEPARTMENT_ANGKOT)
                    }
                }

                ACTION_STOP_SERVICE -> {
                    Timber.d("stop service")
                }
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun postInitialValues() {
        userPosition.postValue(LatLng())
        pathDirection.postValue(mutableListOf())
        destinationPosition.postValue(LatLng())
        angkotPosition.postValue(mutableListOf())
        isTracking.postValue(false)
    }

    @SuppressLint("MissingPermission")
    private fun updateLocationTracking(isTracking: Boolean) {
        if (!MapsUtility.hasLocationPermissions(this)) return
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
                    userPosition.postValue(lastLatLng)
                    Timber.d("NEW LOCATION: ${location.latitude}, ${location.longitude}")
                }
            }
        }
    }

    private fun findDirectionPath()  {
        // TODO
    }

    private fun startForegroundService() {
        val notificationBuilder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setAutoCancel(false)
            .setOngoing(true)
            .setSmallIcon(R.drawable.iconlocation2)
            .setContentTitle("Find Transportation Near You")
            .setContentIntent(pendingIntent())

        startForeground(NOTIFICATION_ID, notificationBuilder.build())
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