package com.miftah.jakasforpassenger.core.data.source.remote.retrofit

import com.miftah.jakasforpassenger.core.data.source.remote.request.LoginRequest
import com.miftah.jakasforpassenger.core.data.source.remote.request.RegisterRequest
import com.miftah.jakasforpassenger.core.data.source.remote.response.LoginResponse
import com.miftah.jakasforpassenger.core.data.source.remote.response.RegisterResponse

interface ApiHelper {
    suspend fun login(loginRequest: LoginRequest) : LoginResponse

    suspend fun register(registerRequest: RegisterRequest) : RegisterResponse
}