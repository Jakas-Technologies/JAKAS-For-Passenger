package com.miftah.jakasforpassenger.ui.transaction

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.miftah.jakasforpassenger.core.data.source.AppRepository
import com.miftah.jakasforpassenger.core.data.source.remote.dto.request.Item
import com.miftah.jakasforpassenger.core.data.source.remote.dto.request.MidtransRequest
import com.miftah.jakasforpassenger.core.data.source.remote.dto.response.DriversItem
import com.miftah.jakasforpassenger.core.data.source.remote.dto.response.FareResponse
import com.miftah.jakasforpassenger.core.data.source.remote.dto.response.SanboxMidtransReponse
import com.miftah.jakasforpassenger.utils.Constants
import com.miftah.jakasforpassenger.utils.Constants.Payment.GOPAY
import com.miftah.jakasforpassenger.utils.Constants.Payment.SHOPEEPAY
import com.miftah.jakasforpassenger.utils.QrScanning
import com.miftah.jakasforpassenger.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TransactionViewModel @Inject constructor(private val repository: AppRepository) :
    ViewModel() {

    private var _angkot = MutableLiveData<DriversItem>()
    val angkot: LiveData<DriversItem> = _angkot

    private var _driverIdentity = MutableLiveData<QrScanning>()
    val driverIdentity: LiveData<QrScanning> = _driverIdentity

    private var _paymentMethode = MutableLiveData<Constants.Payment>()
    val paymentMethode: LiveData<Constants.Payment> = _paymentMethode

    private var _price = MutableLiveData<FareResponse>()
    val price: LiveData<FareResponse> = _price

    private fun price(price : FareResponse) {
        _price.value = price
    }

    fun initActivity(angkot: DriversItem, qrScanning: QrScanning) {
        _angkot.postValue(angkot)
        _driverIdentity.postValue(qrScanning)
    }

    fun choosePayment(payment: Constants.Payment) {
        _paymentMethode.postValue(payment)
    }

    fun midtransRequest(): MidtransRequest? {
        driverIdentity.value ?: return null
        angkot.value ?: return null
        paymentMethode.value ?: return null

        val item = Item(
            id = driverIdentity.value?.id!!,
            name = driverIdentity.value?.name!!,
            price = price.value?.data?.fuelPrice!!,
            quantity = 1
        )
        return MidtransRequest(
            payment_type = paymentMethode.value?.data!!,
            items = listOf(item)
        )
    }

    fun initTransaction(midtrans: MidtransRequest, payment: Constants.Payment): LiveData<Result<SanboxMidtransReponse>> {
        return when (payment) {
            GOPAY -> {
                repository.initTransaction(midtrans)
            }

            SHOPEEPAY -> {
                repository.initTransaction(midtrans)
            }
        }
    }
}