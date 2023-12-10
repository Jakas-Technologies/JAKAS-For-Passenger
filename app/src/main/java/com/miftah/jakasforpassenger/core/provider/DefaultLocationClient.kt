package com.miftah.jakasforpassenger.core.provider

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationManager
import android.os.Looper
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.Priority
import com.miftah.jakasforpassenger.utils.MapsUtility
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class DefaultLocationClient @Inject constructor(
    private val context: Context,
    private val client: FusedLocationProviderClient
) : LocationClient {

    @SuppressLint("MissingPermission")
    override fun getLocationUpdates(interval: Long, fastestInterval: Long): Flow<Location> {
        return callbackFlow {
            if (!MapsUtility.hasLocationPermissions(context)) {
                throw LocationClient.LocationException("Missing location permission")
            }

            val locationManager =
                context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
            val isNetworkEnabled =
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
            if (!isGpsEnabled && !isNetworkEnabled) {
                throw LocationClient.LocationException("GPS is disabled")
            }

            val request = LocationRequest.create().apply {
                this.interval = interval
                this.fastestInterval = fastestInterval
                priority = Priority.PRIORITY_HIGH_ACCURACY
            }

            val locationCallback = object : LocationCallback() {
                override fun onLocationResult(result: LocationResult) {
                    super.onLocationResult(result)
                    result.locations.lastOrNull()?.let { location ->
                        launch { send(location) }
                    }
                }
            }

            client.requestLocationUpdates(
                request,
                locationCallback,
                Looper.getMainLooper()
            )

            awaitClose {
                client.removeLocationUpdates(locationCallback)
            }
        }
    }

    private var locationUser: Location? = null
    @SuppressLint("MissingPermission")
    override fun getLastKnown(): Location? {
        if (!MapsUtility.hasLocationPermissions(context)) {
            throw LocationClient.LocationException("Missing location permission")
        }

        client.lastLocation.addOnSuccessListener { data: Location? ->
            data?.let {
                locationUser = it
            }
        }
        return locationUser
    }
}