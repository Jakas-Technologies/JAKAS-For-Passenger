package com.miftah.jakasforpassenger.utils

import android.Manifest
import android.content.Context
import android.os.Build
import com.google.maps.model.DirectionsResult
import com.google.android.gms.maps.model.LatLng
import com.vmadalin.easypermissions.EasyPermissions


object MapsUtility {
    fun hasLocationPermissions(context: Context) =
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            EasyPermissions.hasPermissions(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        } else {
            EasyPermissions.hasPermissions(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            )
        }

    fun convertToLatLngList(directionsResult: DirectionsResult): List<LatLng> {
        val latLngList: MutableList<LatLng> = ArrayList()
        if (directionsResult.routes.isNotEmpty()) {

            val steps = directionsResult.routes[0].legs[0].steps
            for (step in steps) {
                val stepStartLatLng = step.startLocation
                val stepEndLatLng = step.endLocation
                latLngList.add(LatLng(stepStartLatLng.lat, stepStartLatLng.lng))
                latLngList.add(LatLng(stepEndLatLng.lat, stepEndLatLng.lng))
            }
        }
        return latLngList
    }
}