package com.miftah.jakasforpassenger.utils

import android.graphics.Color
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import java.text.NumberFormat
import java.util.Locale

object Constants {
    const val BASE_URL = "https://34.128.115.212.nip.io/"

    const val REQUEST_CODE_LOCATION_PERMISSION = 0

    const val KEY_MAP = "AIzaSyAttBc9n2uWPiFJ9B79kRsDVU1s6MkdEUs"

    const val POSITION_LAT_LNG = "positionLatLng"
    const val DESTINATION_LAT_LNG = "destinationLatLng"

    const val POLYLINE_COLOR = Color.RED
    const val POLYLINE_WIDTH = 8f
    const val MAP_ZOOM = 18f

    const val ACTION_START_SERVICE = "ACTION_START_SERVICE"
    const val ACTION_STOP_SERVICE = "ACTION_STOP_SERVICE"
    const val ACTION_START_PAYING_SERVICE = "ACTION_START_PAYING_SERVICE"
    const val ACTION_CANCEL_PAYING_SERVICE = "ACTION_CANCEL_PAYING_SERVICE"

    const val NOTIFICATION_CHANNEL_ID = "location_tracker_channel"
    const val NOTIFICATION_CHANNEL_NAME = "Location Tracking"
    const val NOTIFICATION_ID = 1

    const val LOCATION_UPDATE_INTERVAL = 300L
    const val FASTEST_LOCATION_INTERVAL = 100L

    const val EXTRA_POSITION_SERIALIZABLE = "EXTRA_POSITION_SERIALIZABLE"
    const val EXTRA_DESTINATION_SERIALIZABLE = "EXTRA_DESTINATION_SERIALIZABLE"
    const val EXTRA_DEPARTMENT_ANGKOT = "EXTRA_DEPARTMENT_ANGKOT"
    const val EXTRA_IDENTITY_ANGKOT = "EXTRA_IDENTITY_ANGKOT"
    const val EXTRA_QR_CODE = "EXTRA_QR_CODE"
    const val EXTRA_URL_REDIRECT = "EXTRA_URL_REDIRECT"
    const val EXTRA_PRICE = "EXTRA_PRICE"

    const val SHARED_PREFERENCES_KEY = "SHARED_PREFERENCES_KEY"
    val USERNAME = stringPreferencesKey("username")
    val TOKEN_KEY = stringPreferencesKey("token")
    val USER_TYPE_KEY = stringPreferencesKey("userTypeKey")
    val ID_KEY = intPreferencesKey("id")
    val IS_LOGIN_KEY = booleanPreferencesKey("isLogin")

    enum class MapObjective {
        DESTINATION, POSITION
    }

    enum class Payment(val data: String) {
        GOPAY("gopay"), SHOPEEPAY("shopeepay")
    }

    fun formatToRupiah(amount: Int): String {
        val currencyFormat = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
        return currencyFormat.format(amount.toLong())
    }
}