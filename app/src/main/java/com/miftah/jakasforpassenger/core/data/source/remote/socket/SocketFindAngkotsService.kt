package com.miftah.jakasforpassenger.core.data.source.remote.socket

import com.miftah.jakasforpassenger.utils.Angkot
import com.miftah.jakasforpassenger.utils.Resource

interface SocketFindAngkotsService {

    fun initSession(angkotDepartment: Angkot) : Resource<Unit>

    fun getAngkotPosition(callback: (List<String>) -> Unit)

    fun closeConnection() : Resource<Unit>
}