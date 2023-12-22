package com.miftah.jakasforpassenger.core.data.source.remote.socket

import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import com.miftah.jakasforpassenger.core.data.source.remote.dto.response.Coords
import com.miftah.jakasforpassenger.core.data.source.remote.dto.response.DriversItem
import com.miftah.jakasforpassenger.core.data.source.remote.dto.response.GeoGamma
import com.miftah.jakasforpassenger.utils.Constants.BASE_URL
import com.miftah.jakasforpassenger.utils.Resource
import io.socket.client.IO
import io.socket.client.Socket
import org.json.JSONObject
import timber.log.Timber
import java.net.URISyntaxException
import javax.inject.Inject

class SocketUserPositionHandlerImpl @Inject constructor(private val token : String) : SocketUserPositionHandlerService {

    private var socket: Socket? = null

    override fun initSession(): Resource<Unit> {
        return try {
            val option = IO.Options.builder()
                .setExtraHeaders(mapOf("Authorization" to listOf("Bearer $token")))
                .build()
            socket = IO.socket(BASE_URL, option)
            socket?.connect()
            if (socket?.isActive == true) {
                Timber.d("Connect")
                Resource.Success(Unit)
            } else {
                Timber.e("Couldn't establish a connection.")
                Resource.Error("Couldn't establish a connection.")
            }
        } catch (e: URISyntaxException) {
            Timber.e(e)
            Resource.Error(e.localizedMessage ?: "Unknown error")
        }
    }

    override fun sendUserPosition(userPosition: LatLng, angkot : DriversItem) {
        val jsonObject = GeoGamma(
            id = angkot.id,
            coords = Coords(
                altitude = 1.1,
                heading = 1.1,
                latitude = userPosition.latitude,
                accuracy = 1.1,
                altitudeAccuracy = 1.1,
                speed = 1.1,
                longitude = userPosition.longitude
            )
        )
        val gson = Gson()
        val data = JSONObject(gson.toJson(jsonObject))
        socket?.emit("usermove", data)
    }

    override fun getAngkotPosition(callback: (GeoGamma) -> Unit) {
        socket?.on("drivermove") { data ->
            Timber.tag("SOCKET").d("get Loc")
            val gson = Gson()
            val json = data[0].toString()
            val geoGamma: GeoGamma = gson.fromJson(json, GeoGamma::class.java)
            callback(geoGamma)
        }
    }

    override fun closeConnection() {
        socket?.disconnect()
    }

    override fun checkConnection(): Boolean? = socket?.connected()
}