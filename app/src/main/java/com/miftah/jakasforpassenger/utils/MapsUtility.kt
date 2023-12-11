package com.miftah.jakasforpassenger.utils

import android.Manifest
import android.content.Context
import android.os.Build
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Polyline
import com.google.maps.model.DirectionsResult
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

    fun parseDirectionsResult(directionsResult: DirectionsResult): List<LatLng> {
        val path = mutableListOf<LatLng>()

        directionsResult.routes?.forEach { route ->
            route.legs?.forEach { leg ->
                leg.steps?.forEach { step ->
                    path.addAll(decodePolyline(step.polyline.encodedPath))
                }
            }
        }

        return path
    }

    private fun decodePolyline(encodedPolyline: String): List<LatLng> {
        val poly = ArrayList<LatLng>()
        var index = 0
        val len = encodedPolyline.length
        var lat = 0
        var lng = 0

        while (index < len) {
            var result = 1
            var shift = 0
            var b: Int
            do {
                b = encodedPolyline[index++].toInt() - 63 - 1
                result += b shl shift
                shift += 5
            } while (b >= 0x1f)
            lat += if (result and 1 != 0) (result shr 1).inv() else result shr 1

            result = 1
            shift = 0
            do {
                b = encodedPolyline[index++].toInt() - 63 - 1
                result += b shl shift
                shift += 5
            } while (b >= 0x1f)
            lng += if (result and 1 != 0) (result shr 1).inv() else result shr 1

            val latLng = LatLng(lng.toDouble() / 1E5, lat.toDouble() / 1E5)
            poly.add(latLng)
        }

        return poly
    }

    fun convertListLatLngToDouble(listOfLatLng: List<LatLng>): List<Double> {
        val result = mutableListOf<Double>()
        for (latLng in listOfLatLng) {
            result.add(latLng.latitude)
            result.add(latLng.longitude)
        }
        return result
    }

    fun convertListDoubleToLatLng(listOfDouble: List<Double>): List<LatLng> {
        val result = mutableListOf<LatLng>()
        for (i in listOfDouble.indices step 2) {
            result.add(LatLng(listOfDouble[i], listOfDouble[i + 1]))
        }
        return result
    }

    fun convertListDoubleLatLongToLatLng(
        latitudesDouble: List<Double>,
        longitudesDouble: List<Double>
    ): List<LatLng>? {
        if (latitudesDouble.size != longitudesDouble.size) return null

        /*        val latLngList = mutableListOf<LatLng>()

                for (i in latitudesDouble.indices) {
                    val latLng = LatLng(latitudesDouble[i], longitudesDouble[i])
                    latLngList.add(latLng)
                }*/

        val latLngList = latitudesDouble.zip(longitudesDouble) { latitude, longitude ->
            LatLng(latitude, longitude)
        }

        return latLngList
    }

    fun polylineToListLatLng(polyline: Polyline): List<LatLng> {
        val points = polyline.points
        val listLatLng = ArrayList<LatLng>(points.size)
        for (i in points.indices) {
            listLatLng.add(points[i])
        }
        return listLatLng

    }
}