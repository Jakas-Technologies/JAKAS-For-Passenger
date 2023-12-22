package com.miftah.jakasforpassenger.core.data.source.remote.retrofit

import com.miftah.jakasforpassenger.core.data.source.remote.dto.request.DirectionRequest
import com.miftah.jakasforpassenger.core.data.source.remote.dto.request.FarePredict
import com.miftah.jakasforpassenger.core.data.source.remote.dto.request.LoginRequest
import com.miftah.jakasforpassenger.core.data.source.remote.dto.request.MidtransRequest
import com.miftah.jakasforpassenger.core.data.source.remote.dto.request.QrRequest
import com.miftah.jakasforpassenger.core.data.source.remote.dto.request.RegisterPassengerRequest
import com.miftah.jakasforpassenger.core.data.source.remote.dto.response.CancelResponse
import com.miftah.jakasforpassenger.core.data.source.remote.dto.response.DirectionResponse
import com.miftah.jakasforpassenger.core.data.source.remote.dto.response.FareResponse
import com.miftah.jakasforpassenger.core.data.source.remote.dto.response.LoginResponse
import com.miftah.jakasforpassenger.core.data.source.remote.dto.response.LogoutResponse
import com.miftah.jakasforpassenger.core.data.source.remote.dto.response.QrResponse
import com.miftah.jakasforpassenger.core.data.source.remote.dto.response.RegisterPassengerResponse
import com.miftah.jakasforpassenger.core.data.source.remote.dto.response.SanboxMidtransReponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @POST("jakas/api/user/login")
    suspend fun login(
        @Body loginRequest: LoginRequest
    ): LoginResponse

    @POST("jakas/api/user/register")
    suspend fun register(
        @Body registerRequest: RegisterPassengerRequest
    ): RegisterPassengerResponse

    @DELETE("jakas/api/user/{id}/logout")
    suspend fun logout(@Path("id") id: Int): LogoutResponse

    @POST("jakas/api/track/getJurusan")
    suspend fun findAngkotBaseOnPositionAndDestination(
        @Body directionRequest : DirectionRequest,
    ) : DirectionResponse

    @POST("jakas/api/payment/payment")
    suspend fun initTransactionShopee(
        @Body midtransRequest: MidtransRequest
    ): SanboxMidtransReponse

    @POST("jakas/api/payment/start-trip")
    suspend fun qrToMidtrans(
        @Body qrRequest: QrRequest
    ) : QrResponse

    @DELETE("jakas/api/payment/cancel-trip")
    suspend fun midtransCancel() : CancelResponse

    @POST("jakas/api/fare/predict")
    suspend fun getPredictFare(@Body fareRequest : FarePredict) : FareResponse
}