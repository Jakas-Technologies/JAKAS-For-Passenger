package com.miftah.jakasforpassenger.core.data.source.remote.retrofit

import com.miftah.jakasforpassenger.core.data.source.remote.dto.request.LoginRequest
import com.miftah.jakasforpassenger.core.data.source.remote.dto.request.RegisterPassengerRequest
import com.miftah.jakasforpassenger.core.data.source.remote.dto.response.LoginResponse
import com.miftah.jakasforpassenger.core.data.source.remote.dto.response.RegisterPassengerResponse
import javax.inject.Inject

class ApiServiceImpl @Inject constructor(
    private val apiService: ApiService
) : ApiHelper {
    override suspend fun login(loginRequest: LoginRequest): LoginResponse =
        apiService.login(loginRequest)

    override suspend fun register(registerRequest: RegisterPassengerRequest): RegisterPassengerResponse =
        apiService.register(registerRequest)


}