package com.miftah.jakasforpassenger.core.data.source.remote.retrofit

import com.miftah.jakasforpassenger.core.data.source.remote.dto.request.LoginRequest
import com.miftah.jakasforpassenger.core.data.source.remote.dto.request.RegisterPassengerRequest
import com.miftah.jakasforpassenger.core.data.source.remote.dto.response.LoginResponse
import com.miftah.jakasforpassenger.core.data.source.remote.dto.response.RegisterPassengerResponse

interface ApiHelper {
    suspend fun login(loginRequest: LoginRequest): LoginResponse

    suspend fun register(registerRequest: RegisterPassengerRequest): RegisterPassengerResponse

    // TODO fetch from CC angkot apa saja

}