package com.miftah.jakasforpassenger.core.data.source.remote.retrofit

import com.miftah.jakasforpassenger.core.data.source.remote.dto.request.MidtransRequest
import com.miftah.jakasforpassenger.core.data.source.remote.dto.request.QrRequest
import com.miftah.jakasforpassenger.core.data.source.remote.dto.response.CancelResponse
import com.miftah.jakasforpassenger.core.data.source.remote.dto.response.QrResponse
import com.miftah.jakasforpassenger.core.data.source.remote.dto.response.SanboxMidtransReponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.POST

interface MidtransApiService {

/*
    @POST("gopay")
    suspend fun initTransactionGopay(
        @Body midtransRequest: MidtransRequest
    ): SanboxMidtransReponse
*/

    @POST("payment")
    suspend fun initTransactionShopee(
        @Body midtransRequest: MidtransRequest
    ): SanboxMidtransReponse


    @POST("start-trip")
    suspend fun qrToMidtrans(
        @Body qrRequest: QrRequest
    ) : QrResponse


    @DELETE("cancel-trip")
    suspend fun midtransCancel() : CancelResponse

}