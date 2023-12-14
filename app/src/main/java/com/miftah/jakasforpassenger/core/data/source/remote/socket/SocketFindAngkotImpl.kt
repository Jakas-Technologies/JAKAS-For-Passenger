package com.miftah.jakasforpassenger.core.data.source.remote.socket

import com.miftah.jakasforpassenger.utils.Angkot
import com.miftah.jakasforpassenger.utils.Resource
import io.socket.client.IO
import io.socket.client.Socket
import timber.log.Timber
import javax.inject.Inject

class SocketFindAngkotImpl @Inject constructor() : SocketFindAngkotsService {

    private var socket: Socket? = null

    override fun initSession(angkotDepartment: Angkot): Resource<Unit> {
        return try {
            val token =
                "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6MiwiZmlyc3ROYW1lIjoiZ2FtbWEiLCJsYXN0TmFtZSI6InJpenF1aGEiLCJlbWFpbCI6ImdhbW1hQGVtYWlsLmNvbSIsInJvbGUiOiJ1c2VyIiwicGFzc3dvcmQiOiJ3b3JkIiwiY3JlYXRlZEF0IjoiMjAyMy0xMi0xM1QxNTo1MTozMi44NzVaIiwidXBkYXRlZEF0IjoiMjAyMy0xMi0xM1QxNTo1MTozMi44NzVaIiwiaWF0IjoxNzAyNDgyNzk1fQ.Ad_KUePAh1c9AF5wRgR1wfQG_DlyTVDd1_SBsn7aj1k"
            val option =
                IO.Options.builder().setExtraHeaders(mapOf("auth" to listOf("Bearer $token")))
                    .build()
            socket = IO.socket("http://34.128.115.212:3000/?angkot=${angkotDepartment.id}", option)
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

    override fun getAngkotPosition(callback: (List<String>) -> Unit) {
        socket?.on("angkot-move") { data ->
            Timber.d("Get Loc")
            callback(data.map { it as String })
        }
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