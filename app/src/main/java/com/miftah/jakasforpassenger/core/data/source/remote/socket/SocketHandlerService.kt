package com.miftah.jakasforpassenger.core.data.source.remote.socket

import com.google.android.gms.maps.model.LatLng
import com.miftah.jakasforpassenger.utils.Angkot
import com.miftah.jakasforpassenger.utils.Resource

interface SocketHandlerService {

    fun initSession(angkotDepartment: Angkot) : Resource<Unit>

    fun getAngkotPosition(callback: (List<String>) -> Unit)

    fun sendUserPosition(userPosition: LatLng)

    fun establishConnection()

    fun closeConnection()
}

/*
    lateinit var mSocket: Socket

    @Synchronized
    fun setSocket() {
        try {
            // "http://10.0.2.2:3000" is the network your Android emulator must use to join the localhost network on your computer
            // "http://localhost:3000/" will not work
            // If you want to use your physical phone you could use your ip address plus :3000
            // This will allow your Android Emulator and physical device at your home to connect to the server
            mSocket = IO.socket("http://10.0.2.2:3000")
        } catch (e: URISyntaxException) {

        }
    }

    @Synchronized
    fun getSocket(): Socket {
        return mSocket
    }

    @Synchronized
    fun establishConnection() {
        mSocket.connect()
    }

    @Synchronized
    fun closeConnection() {
        mSocket.disconnect()
    }
 */