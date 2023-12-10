package com.miftah.jakasforpassenger.core.workers

import android.annotation.SuppressLint
import android.content.Context
import android.os.Looper
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.Priority
import com.miftah.jakasforpassenger.utils.Constants
import com.miftah.jakasforpassenger.utils.MapsUtility
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class FindUserWorker @AssistedInject constructor(
    private val fusedLocationProviderClient: FusedLocationProviderClient,
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        return Result.success()
    }

    @SuppressLint("MissingPermission")
    private fun updateLocationTracking(isTracking: Boolean) {
        if (!MapsUtility.hasLocationPermissions(applicationContext)) return

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

        }
    }

}