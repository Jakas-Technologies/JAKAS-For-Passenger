package com.miftah.jakasforpassenger.core.data.source.remote.retrofit

import com.miftah.jakasforpassenger.core.data.source.remote.dto.request.MidtransRequest
import com.miftah.jakasforpassenger.core.data.source.remote.dto.response.SanboxMidtransReponse
import retrofit2.http.Body
import retrofit2.http.POST

interface MidtransApiService {

    @POST("gopay")
    suspend fun initTransactionGopay(
        @Body midtransRequest: MidtransRequest
    ): SanboxMidtransReponse

    @POST("shopee")
    suspend fun initTransactionShopee(
        @Body midtransRequest: MidtransRequest
    ): SanboxMidtransReponse
}