package com.miftah.jakasforpassenger.core.data.source.remote.retrofit

import com.miftah.jakasforpassenger.core.data.source.remote.dto.request.FarePredict
import com.miftah.jakasforpassenger.core.data.source.remote.dto.request.LoginRequest
import com.miftah.jakasforpassenger.core.data.source.remote.dto.request.MidtransRequest
import com.miftah.jakasforpassenger.core.data.source.remote.dto.request.QrRequest
import com.miftah.jakasforpassenger.core.data.source.remote.dto.request.RegisterPassengerRequest
import com.miftah.jakasforpassenger.core.data.source.remote.dto.response.CancelResponse
import com.miftah.jakasforpassenger.core.data.source.remote.dto.response.FareResponse
import com.miftah.jakasforpassenger.core.data.source.remote.dto.response.LoginResponse
import com.miftah.jakasforpassenger.core.data.source.remote.dto.response.LogoutResponse
import com.miftah.jakasforpassenger.core.data.source.remote.dto.response.QrResponse
import com.miftah.jakasforpassenger.core.data.source.remote.dto.response.RegisterPassengerResponse
import com.miftah.jakasforpassenger.core.data.source.remote.dto.response.SanboxMidtransReponse
import javax.inject.Inject

class ApiServiceImpl @Inject constructor(
    private val apiService: ApiService
) : ApiHelper {
    override suspend fun login(loginRequest: LoginRequest): LoginResponse =
        apiService.login(loginRequest)

    override suspend fun register(registerRequest: RegisterPassengerRequest): RegisterPassengerResponse =
        apiService.register(registerRequest)

    override suspend fun logout(id: Int): LogoutResponse =
        apiService.logout(id)

    override suspend fun initTransactionShopee(midtransRequest: MidtransRequest): SanboxMidtransReponse =
        apiService.initTransactionShopee(midtransRequest)

    override suspend fun qrToMidtrans(qrRequest: QrRequest): QrResponse =
        apiService.qrToMidtrans(qrRequest)

    override suspend fun midtransCancel(): CancelResponse =
        apiService.midtransCancel()

    override suspend fun getPredictFare(fareRequest: FarePredict): FareResponse =
        apiService.getPredictFare(fareRequest)


}