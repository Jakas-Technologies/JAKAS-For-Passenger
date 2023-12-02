package com.miftah.jakasforpassenger.core.work

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.android.gms.common.api.ApiException
import com.google.maps.DirectionsApi
import com.google.maps.GeoApiContext
import com.google.maps.model.DirectionsResult
import com.google.maps.model.TravelMode
import com.miftah.jakasforpassenger.utils.Constants.DESTINATION_LAT_LNG
import com.miftah.jakasforpassenger.utils.Constants.KEY_MAP
import com.miftah.jakasforpassenger.utils.Constants.POSITION_LAT_LNG
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import timber.log.Timber
import java.io.IOException

@HiltWorker
class FindRouteWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {
    override suspend fun doWork(): Result {
        val positionLatLng = inputData.getString(POSITION_LAT_LNG)
        val destinationLatLng = inputData.getString(DESTINATION_LAT_LNG)
        val geoApiContext = GeoApiContext.Builder().apiKey(KEY_MAP).build()

        return findRoute(positionLatLng, destinationLatLng, geoApiContext)
    }

    private fun findRoute(
        origin: String?,
        destination: String?,
        geoApiContext: GeoApiContext
    ): Result {
        return try {
            workRouteResult = DirectionsApi
                .newRequest(geoApiContext)
                .origin(origin)
                .destination(destination)
                .mode(TravelMode.DRIVING)
                .await()
            Result.success()
        } catch (e: ApiException) {
            Timber.e(e)
            Result.failure()
        } catch (e: InterruptedException) {
            Timber.e(e)
            Result.failure()
        } catch (e: IOException) {
            Timber.e(e)
            Result.failure()
        }
    }

    companion object {
        var workRouteResult : DirectionsResult? = null
    }
}