package com.miftah.jakasforpassenger.core.data.source.remote.socket

import androidx.lifecycle.MutableLiveData
import com.miftah.jakasforpassenger.utils.Angkot
import io.socket.client.IO
import io.socket.client.Socket
import timber.log.Timber
import javax.inject.Inject

class SocketFindAngkotImpl @Inject constructor() : SocketFindAngkotsService {

    private lateinit var socket: Socket
    private var isConnected = false

    override fun initSession(angkotDepartment: Angkot) {
        val token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6MywiZmlyc3ROYW1lIjoidGVzdCIsImxhc3ROYW1lIjoiYWNjb3VudCIsImVtYWlsIjoidGVzdEBlbWFpbC5jb20iLCJyb2xlIjoidXNlciIsInBhc3N3b3JkIjoidGVzdCIsImNyZWF0ZWRBdCI6IjIwMjMtMTItMTdUMTM6Mjc6MzkuNDM0WiIsInVwZGF0ZWRBdCI6IjIwMjMtMTItMTdUMTM6Mjc6MzkuNDM0WiIsImlhdCI6MTcwMjk3MzM2NH0.rX4adroocIl9JO46Rfi2cAWsYbhFaIDKtdlvj1EbcA4"
        val option = IO.Options.builder()
            .setExtraHeaders(mapOf("auth" to listOf("Bearer $token")))
            .build()
        socket = IO.socket("https://34.128.115.212.nip.io/", option)

        socket.on(Socket.EVENT_CONNECT) {
            data.postValue(true)
        }

        socket.on(Socket.EVENT_DISCONNECT) {
            data.postValue(false)
        }
    }

    companion object {
        val data = MutableLiveData<Boolean>()
    }

    override suspend fun getAngkotPosition(callback: (List<String>) -> Unit) {
        socket.on("angkot-move") { data ->
            Timber.d("Get Loc")

        }
    }

    override fun closeConnection() {
        socket.disconnect()
    }

}