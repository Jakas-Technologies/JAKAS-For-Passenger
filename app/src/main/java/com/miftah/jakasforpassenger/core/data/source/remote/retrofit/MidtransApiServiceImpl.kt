package com.miftah.jakasforpassenger.core.data.source.remote.retrofit

import com.miftah.jakasforpassenger.core.data.source.remote.dto.request.MidtransRequest
import com.miftah.jakasforpassenger.core.data.source.remote.dto.request.QrRequest
import com.miftah.jakasforpassenger.core.data.source.remote.dto.response.CancelResponse
import com.miftah.jakasforpassenger.core.data.source.remote.dto.response.QrResponse
import com.miftah.jakasforpassenger.core.data.source.remote.dto.response.SanboxMidtransReponse
import javax.inject.Inject

class MidtransApiServiceImpl @Inject constructor(
    private val apiService: MidtransApiService
) : MidtransApiHelper {
/*    override suspend fun initTransactionGopay(midtransRequest: MidtransRequest): SanboxMidtransReponse =
        apiService.initTransactionGopay(midtransRequest)*/

    override suspend fun initTransactionShopee(midtransRequest: MidtransRequest): SanboxMidtransReponse =
        apiService.initTransactionShopee(midtransRequest)

    override suspend fun qrToMidtrans(qrRequest: QrRequest): QrResponse =
        apiService.qrToMidtrans(qrRequest)

    override suspend fun midtransCancel(): CancelResponse =
        apiService.midtransCancel()
}