package com.miftah.jakasforpassenger.utils

import android.graphics.Color

object Constants {
    const val BASE_URL = "http://34.101.89.120:4000/"

    const val REQUEST_CODE_LOCATION_PERMISSION = 0

    const val KEY_MAP = "AIzaSyAttBc9n2uWPiFJ9B79kRsDVU1s6MkdEUs"

    const val POSITION_LAT_LNG = "positionLatLng"
    const val DESTINATION_LAT_LNG = "destinationLatLng"

    const val POLYLINE_COLOR = Color.RED
    const val POLYLINE_WIDTH = 8f
    const val MAP_ZOOM = 15f

    const val ACTION_START_SERVICE = "ACTION_START_SERVICE"
    const val ACTION_STOP_SERVICE = "ACTION_STOP_SERVICE"

    const val NOTIFICATION_CHANNEL_ID = "location_tracker_channel"
    const val NOTIFICATION_CHANNEL_NAME = "Location Tracking"
    const val NOTIFICATION_ID = 1

    const val LOCATION_UPDATE_INTERVAL = 5000L
    const val FASTEST_LOCATION_INTERVAL = 2000L

    const val EXTRA_POSITION_SERIALIZABLE = "EXTRA_POSITION_SERIALIZABLE"
    const val EXTRA_DESTINATION_SERIALIZABLE = "EXTRA_DESTINATION_SERIALIZABLE"
    const val EXTRA_DEPARTMENT_ANGKOT = "EXTRA_DEPARTMENT_ANGKOT"

    const val KEY_DIRECTION_LONGITUDES = "KEY_DIRECTION_LONGITUDES"
    const val KEY_DIRECTION_LATITUDES = "KEY_DIRECTION_LATITUDE"

    enum class MapObjective {
        DESTINATION, POSITION
    }
}