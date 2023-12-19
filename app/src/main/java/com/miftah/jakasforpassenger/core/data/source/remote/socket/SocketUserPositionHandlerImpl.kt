package com.miftah.jakasforpassenger.core.data.source.remote.socket

import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import com.miftah.jakasforpassenger.core.data.source.remote.dto.response.Coords
import com.miftah.jakasforpassenger.core.data.source.remote.dto.response.GeoGamma
import com.miftah.jakasforpassenger.utils.Resource
import io.socket.client.IO
import io.socket.client.Socket
import org.json.JSONObject
import timber.log.Timber
import javax.inject.Inject

class SocketUserPositionHandlerImpl @Inject constructor() : SocketUserPositionHandlerService {

    private var socket: Socket? = null

    override fun initSession(): Resource<Unit> {
        return try {
            val token =
                "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6MywiZmlyc3ROYW1lIjoidGVzdCIsImxhc3ROYW1lIjoiYWNjb3VudCIsImVtYWlsIjoidGVzdEBlbWFpbC5jb20iLCJyb2xlIjoidXNlciIsInBhc3N3b3JkIjoidGVzdCIsImNyZWF0ZWRBdCI6IjIwMjMtMTItMTdUMTM6Mjc6MzkuNDM0WiIsInVwZGF0ZWRBdCI6IjIwMjMtMTItMTdUMTM6Mjc6MzkuNDM0WiIsImlhdCI6MTcwMjk3MzM2NH0.rX4adroocIl9JO46Rfi2cAWsYbhFaIDKtdlvj1EbcA4"
            val option = IO.Options.builder()
                .setExtraHeaders(mapOf("auth" to listOf("Bearer $token")))
                .build()
            socket = IO.socket("https://34.128.115.212.nip.io/", option)
            socket?.connect()
            if (socket?.isActive == true) {
                Timber.d("Connect")
                Resource.Success(Unit)
            } else {
                Timber.e("Couldn't establish a connection.")
                Resource.Error("Couldn't establish a connection.")
            }
        } catch (e: Exception) {
            Timber.e(e)
            Resource.Error(e.localizedMessage ?: "Unknown error")
        }
    }

    override fun sendUserPosition(userPosition: LatLng) {
        val jsonObject = GeoGamma(
            id = 1,
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

//        Timber.d("Share Loc")
        val gson = Gson()
        val data = JSONObject(gson.toJson(jsonObject))
        socket?.emit("usermove", data)
    }

    override fun getAngkotPosition(callback: (List<String>) -> Unit) {
        socket?.on("drivermove") { data ->
            Timber.tag("SOCKET").d("get Loc")
            callback(data.map { it as String })
        }
    }

    override fun closeConnection() {
        socket?.disconnect()
    }

    override fun checkConnection(): Boolean? = socket?.connected()
}