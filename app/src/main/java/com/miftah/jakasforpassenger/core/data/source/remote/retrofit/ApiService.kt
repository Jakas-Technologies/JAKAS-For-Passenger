package com.miftah.jakasforpassenger.core.data.source.remote.retrofit

import com.google.android.gms.maps.model.LatLng
import com.miftah.jakasforpassenger.core.data.source.remote.dto.request.LoginRequest
import com.miftah.jakasforpassenger.core.data.source.remote.dto.request.RegisterRequest
import com.miftah.jakasforpassenger.core.data.source.remote.dto.response.LoginResponse
import com.miftah.jakasforpassenger.core.data.source.remote.dto.response.RegisterResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("login")
    suspend fun login(
        @Body loginRequest: LoginRequest
    ): LoginResponse

    @POST("register")
    suspend fun register(
        @Body registerRequest: RegisterRequest
    ): RegisterResponse

    @POST("")
    suspend fun findAngkotBaseOnPositionAndDestination(
        @Body latitude : LatLng,
        @Body longitude : LatLng
    ) : LoginResponse

}