package com.miftah.jakasforpassenger.core.services

import android.app.PendingIntent
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.MutableLiveData
import com.google.maps.model.LatLng
import com.miftah.jakasforpassenger.R
import com.miftah.jakasforpassenger.ui.maps.MapsActivity
import com.miftah.jakasforpassenger.utils.Constants.ACTION_START_SERVICE
import com.miftah.jakasforpassenger.utils.Constants.ACTION_STOP_SERVICE
import com.miftah.jakasforpassenger.utils.Constants.NOTIFICATION_CHANNEL_ID
import com.miftah.jakasforpassenger.utils.Constants.NOTIFICATION_ID
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class LocationTrackerService : LifecycleService() {

    private var serviceKilled = false

    companion object {
        val userPosition = MutableLiveData<LatLng>()

    }

    override fun onCreate() {
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            when (it.action) {
                ACTION_START_SERVICE -> {
                    Timber.d("Start Service")
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