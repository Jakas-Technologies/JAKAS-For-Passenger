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
                "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6MiwiZmlyc3ROYW1lIjoiZ2FtbWEiLCJsYXN0TmFtZSI6InJpenF1aGEiLCJlbWFpbCI6ImdhbW1hQGVtYWlsLmNvbSIsInJvbGUiOiJ1c2VyIiwicGFzc3dvcmQiOiJ3b3JkIiwiY3JlYXRlZEF0IjoiMjAyMy0xMi0xM1QxNTo1MTozMi44NzVaIiwidXBkYXRlZEF0IjoiMjAyMy0xMi0xM1QxNTo1MTozMi44NzVaIiwiaWF0IjoxNzAyNDgyNzk1fQ.Ad_KUePAh1c9AF5wRgR1wfQG_DlyTVDd1_SBsn7aj1k"
            val option = IO.Options.builder()
                .setExtraHeaders(mapOf("auth" to listOf("Bearer $token")))
                .build()
            socket = IO.socket("http://34.128.115.212:3000/", option)
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

    /*
    socketHandler.onGeolocationData { jsonDataList ->

    jsonDataList.forEach { jsonData ->

        val geoJson = Gson().fromJson(jsonData, GeoJsonObject::class.java)

        // Process the GeoJSON data here

    }
}
    * */

    override fun sendUserPosition(userPosition: LatLng) {
        /*        val position = listOf(userPosition.latitude, userPosition.latitude)
                val data = GeoJSON(
                    geometry = Geometry(position, "Point"),
                    type = "User Position",
                    properties = Properties("User Destination")
                )*/
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
//        val data = Json.encodeToString(jsonObject)
        val gson = Gson()
        val data = JSONObject(gson.toJson(jsonObject))
        socket?.emit("user-move", data)
    }

    override fun closeConnection(): Resource<Unit> {
        return try {
            socket?.disconnect()
            if (socket?.isActive == false) {
                Timber.d("Connect")
                Resource.Success(Unit)
            } else {
                Timber.e("Couldn't close a connection.")
                Resource.Error("Couldn't close a connection.")
            }
        } catch (e: Exception) {
            Timber.e(e)
            Resource.Error(e.localizedMessage ?: "Unknown error")
        }
    }
}