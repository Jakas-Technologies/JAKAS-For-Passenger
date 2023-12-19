package com.miftah.jakasforpassenger.core.data.source.remote.retrofit

import com.miftah.jakasforpassenger.core.data.source.remote.dto.request.MidtransRequest
import com.miftah.jakasforpassenger.core.data.source.remote.dto.response.SanboxMidtransReponse

interface MidtransApiHelper {
    suspend fun initTransactionGopay(midtransRequest: MidtransRequest) : SanboxMidtransReponse

    suspend fun initTransactionShopee(midtransRequest: MidtransRequest) : SanboxMidtransReponse

}