package com.miftah.jakasforpassenger.ui.qrscanner

import androidx.lifecycle.ViewModel
import com.miftah.jakasforpassenger.core.data.source.AppRepository
import com.miftah.jakasforpassenger.core.data.source.remote.dto.request.QrRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class QrCodeScannerViewModel @Inject constructor(private val repository: AppRepository) : ViewModel() {

    fun qrToMidtrans(qrToMidtransRequest: QrRequest) = repository.qrToMidtrans(qrToMidtransRequest)

}