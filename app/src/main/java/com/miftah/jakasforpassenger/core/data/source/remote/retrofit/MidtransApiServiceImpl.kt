package com.miftah.jakasforpassenger.core.data.source.remote.retrofit

import com.miftah.jakasforpassenger.core.data.source.remote.dto.request.MidtransRequest
import com.miftah.jakasforpassenger.core.data.source.remote.dto.response.SanboxMidtransReponse
import javax.inject.Inject

class MidtransApiServiceImpl @Inject constructor(
    private val apiService: MidtransApiService
) : MidtransApiHelper {
    override suspend fun initTransactionGopay(midtransRequest: MidtransRequest): SanboxMidtransReponse =
        apiService.initTransactionGopay(midtransRequest)

    override suspend fun initTransactionShopee(midtransRequest: MidtransRequest): SanboxMidtransReponse =
        apiService.initTransactionShopee(midtransRequest)


}