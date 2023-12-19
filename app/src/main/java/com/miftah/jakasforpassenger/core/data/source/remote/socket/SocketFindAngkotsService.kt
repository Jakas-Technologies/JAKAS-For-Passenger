package com.miftah.jakasforpassenger.core.data.source.remote.socket

import com.miftah.jakasforpassenger.utils.Angkot

interface SocketFindAngkotsService {

    fun initSession(angkotDepartment: Angkot)

    suspend fun getAngkotPosition(callback: (List<String>) -> Unit)

    fun closeConnection()
}