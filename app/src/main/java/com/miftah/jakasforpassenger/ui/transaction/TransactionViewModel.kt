package com.miftah.jakasforpassenger.ui.transaction

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.miftah.jakasforpassenger.core.data.source.AppRepository
import com.miftah.jakasforpassenger.core.data.source.remote.dto.request.MidtransRequest
import com.miftah.jakasforpassenger.utils.Angkot
import com.miftah.jakasforpassenger.utils.QrScanning
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TransactionViewModel @Inject constructor(private val repository: AppRepository) : ViewModel() {

    private var _angkot = MutableLiveData<Angkot>()
    val angkot: LiveData<Angkot> = _angkot

    private var _driverIdentity = MutableLiveData<QrScanning>()
    val driverIdentity: LiveData<QrScanning> = _driverIdentity

    private var _paymentMethode = MutableLiveData<String>()
    val paymentMethode: LiveData<String> = _paymentMethode

    fun initActivity(angkot : Angkot, qrScanning: QrScanning) {
        _angkot.postValue(angkot)
        _driverIdentity.postValue(qrScanning)
    }

    fun choosePayment(payment : String) {
        _paymentMethode.postValue(payment)
    }

    fun initTransactionGopay(midtrans : MidtransRequest) = repository.initGopay(midtrans)
}