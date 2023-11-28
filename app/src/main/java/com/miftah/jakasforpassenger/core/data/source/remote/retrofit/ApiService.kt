package com.miftah.jakasforpassenger.core.data.source.remote.retrofit

import com.miftah.jakasforpassenger.core.data.source.remote.response.LoginResponse
import com.miftah.jakasforpassenger.core.data.source.remote.response.RegisterResponse
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiService {
    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): LoginResponse

    @POST("register")
    suspend fun register(
        @Body params : RequestBody
    ): RegisterResponse
}