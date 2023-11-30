package com.miftah.jakasforpassenger.core.data.source.remote.retrofit

import com.miftah.jakasforpassenger.core.data.source.remote.request.LoginRequest
import com.miftah.jakasforpassenger.core.data.source.remote.request.RegisterRequest
import com.miftah.jakasforpassenger.core.data.source.remote.response.LoginResponse
import com.miftah.jakasforpassenger.core.data.source.remote.response.RegisterResponse
import javax.inject.Inject

class ApiServiceImpl @Inject constructor(
    private val apiService: ApiService
) : ApiHelper {
    override suspend fun login(loginRequest: LoginRequest): LoginResponse =
        apiService.login(loginRequest)

    override suspend fun register(registerRequest: RegisterRequest): RegisterResponse =
        apiService.register(registerRequest)

}