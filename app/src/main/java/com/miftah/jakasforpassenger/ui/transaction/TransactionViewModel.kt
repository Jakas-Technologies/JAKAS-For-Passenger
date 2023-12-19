package com.miftah.jakasforpassenger.ui.transaction

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.miftah.jakasforpassenger.core.data.source.AppRepository
import com.miftah.jakasforpassenger.core.data.source.remote.dto.request.Item
import com.miftah.jakasforpassenger.core.data.source.remote.dto.request.MidtransRequest
import com.miftah.jakasforpassenger.core.data.source.remote.dto.response.SanboxMidtransReponse
import com.miftah.jakasforpassenger.utils.Angkot
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

    private var _angkot = MutableLiveData<Angkot>()
    val angkot: LiveData<Angkot> = _angkot

    private var _driverIdentity = MutableLiveData<QrScanning>()
    val driverIdentity: LiveData<QrScanning> = _driverIdentity

    private var _paymentMethode = MutableLiveData<Constants.Payment>()
    val paymentMethode: LiveData<Constants.Payment> = _paymentMethode

    fun initActivity(angkot: Angkot, qrScanning: QrScanning) {
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
            price = angkot.value?.price!!,
            quantity = 1
        )
        return MidtransRequest(
            payment_type = paymentMethode.value?.name!!,
            items = listOf(item)
        )
    }

    fun initTransaction(midtrans: MidtransRequest, payment: Constants.Payment): LiveData<Result<SanboxMidtransReponse>> {
        when (payment) {
            GOPAY -> {
                return repository.initGopay(midtrans)
            }

            SHOPEEPAY -> {
                return repository.initShopee(midtrans)
            }
        }
    }
}