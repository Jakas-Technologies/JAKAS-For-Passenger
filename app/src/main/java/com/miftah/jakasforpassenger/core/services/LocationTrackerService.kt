package com.miftah.jakasforpassenger.core.services

import android.app.PendingIntent
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY
import com.miftah.jakasforpassenger.R
import com.miftah.jakasforpassenger.ui.maps.MapsActivity
import com.miftah.jakasforpassenger.utils.Constants.ACTION_PAUSE_SERVICE
import com.miftah.jakasforpassenger.utils.Constants.ACTION_START_OR_RESUME_SERVICE
import com.miftah.jakasforpassenger.utils.Constants.ACTION_STOP_SERVICE
import com.miftah.jakasforpassenger.utils.Constants.FASTEST_LOCATION_INTERVAL
import com.miftah.jakasforpassenger.utils.Constants.LOCATION_UPDATE_INTERVAL
import com.miftah.jakasforpassenger.utils.Constants.NOTIFICATION_CHANNEL_ID
import com.miftah.jakasforpassenger.utils.Constants.NOTIFICATION_ID
import com.miftah.jakasforpassenger.utils.MapsUtility
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class LocationTrackerService : LifecycleService() {

    private var isFirstRun = true

    lateinit var fusedLocationProviderClient : FusedLocationProviderClient

    override fun onCreate() {
        super.onCreate()
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            when (it.action) {
                ACTION_START_OR_RESUME_SERVICE -> {
                    if (isFirstRun) {
                        startForegroundService()
                        isFirstRun = false
                    } else {
                        Timber.d("Resume Service")
                    }
                }

                ACTION_PAUSE_SERVICE -> {
                    Timber.d("start service")
                }

                ACTION_STOP_SERVICE -> {
                    Timber.d("stop service")
                }
            }
        }
        return super.onStartCommand(intent, flags, startId)
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

    private fun updateLocationTracking(isTracking: Boolean) {
        if (isTracking) {
            if (MapsUtility.hasLocationPermissions(this)) {
                val request = LocationRequest.create().apply {
                    interval = LOCATION_UPDATE_INTERVAL
                    fastestInterval = FASTEST_LOCATION_INTERVAL
                    priority = PRIORITY_HIGH_ACCURACY
                }
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