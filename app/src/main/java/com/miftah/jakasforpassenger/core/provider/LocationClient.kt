package com.miftah.jakasforpassenger.core.provider

import android.location.Location
import kotlinx.coroutines.flow.Flow

interface LocationClient {
    fun getLocationUpdates(interval: Long, fastestInterval : Long): Flow<Location>

    fun getLastKnown(): Location?

    class LocationException(message: String): Exception()
}