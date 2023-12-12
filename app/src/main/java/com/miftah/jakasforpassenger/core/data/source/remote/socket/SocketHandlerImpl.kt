package com.miftah.jakasforpassenger.core.data.source.remote.socket

import com.google.android.gms.maps.model.LatLng
import com.miftah.jakasforpassenger.core.data.source.remote.dto.response.Coords
import com.miftah.jakasforpassenger.core.data.source.remote.dto.response.GeoGamma
import com.miftah.jakasforpassenger.core.data.source.remote.dto.response.GeoJSON
import com.miftah.jakasforpassenger.core.data.source.remote.dto.response.Geometry
import com.miftah.jakasforpassenger.core.data.source.remote.dto.response.Properties
import com.miftah.jakasforpassenger.utils.Angkot
import com.miftah.jakasforpassenger.utils.Resource
import io.socket.client.IO
import io.socket.client.Socket
import timber.log.Timber
import javax.inject.Inject

class SocketHandlerImpl @Inject constructor() : SocketHandlerService {

    private var socket: Socket? = null

    override fun initSession(angkotDepartment: Angkot): Resource<Unit> {
        return try {
            val token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6MiwiZmlyc3ROYW1lIjoiZ2FtbWEiLCJsYXN0TmFtZSI6InJpenF1aGEiLCJlbWFpbCI6ImdhbW1hQGVtYWlsLmNvbSIsInBhc3N3b3JkIjoid29yZCIsImNyZWF0ZWRBdCI6IjIwMjMtMTItMTJUMTE6MTE6MzAuNDMxWiIsInVwZGF0ZWRBdCI6IjIwMjMtMTItMTJUMTE6MTE6MzAuNDMxWiIsImlhdCI6MTcwMjM5NTExM30.Yd0Y2Y45L0IsLrSMouC22ov3WRj6Ls3MHi2vs7Qbins"
            val option = IO.Options.builder().setExtraHeaders(mapOf("auth" to listOf("Bearer $token"))).build()
            socket = IO.socket("http://34.128.67.252:3000/",option)
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

    override fun getAngkotPosition(callback: (List<String>) -> Unit) {
        socket?.on("user-move") { data ->
            Timber.d("Get Loc")
            callback(data.map { it as String })
        }
    }

    /*
    socketHandler.onGeolocationData { jsonDataList ->

    jsonDataList.forEach { jsonData ->

        val geoJson = Gson().fromJson(jsonData, GeoJsonObject::class.java)

        // Process the GeoJSON data here

    }
}
    * */

    override fun sendUserPosition(userPosition: LatLng) {
        val position = listOf(userPosition.latitude, userPosition.latitude)
        val data = GeoJSON(
            geometry = Geometry(position, "Point"),
            type = "User Position",
            properties = Properties("User Destination")
        )
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

        Timber.d("Share Loc")
        socket?.emit("user-move", jsonObject)
    }

    override fun establishConnection() {
        socket?.connect()
    }

    override fun closeConnection() {
        socket?.disconnect()
    }
}