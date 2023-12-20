package com.miftah.jakasforpassenger.core.data.source

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.google.android.gms.maps.model.LatLng
import com.miftah.jakasforpassenger.core.data.source.remote.dto.request.LoginRequest
import com.miftah.jakasforpassenger.core.data.source.remote.dto.request.MidtransRequest
import com.miftah.jakasforpassenger.core.data.source.remote.dto.request.QrRequest
import com.miftah.jakasforpassenger.core.data.source.remote.dto.request.RegisterPassengerRequest
import com.miftah.jakasforpassenger.core.data.source.remote.dto.response.CancelResponse
import com.miftah.jakasforpassenger.core.data.source.remote.dto.response.LoginResponse
import com.miftah.jakasforpassenger.core.data.source.remote.dto.response.QrResponse
import com.miftah.jakasforpassenger.core.data.source.remote.dto.response.RegisterPassengerResponse
import com.miftah.jakasforpassenger.core.data.source.remote.dto.response.SanboxMidtransReponse
import com.miftah.jakasforpassenger.core.data.source.remote.retrofit.ApiService
import com.miftah.jakasforpassenger.core.data.source.remote.retrofit.MidtransApiService
import com.miftah.jakasforpassenger.core.dummy.Dummy
import com.miftah.jakasforpassenger.utils.Angkot
import com.miftah.jakasforpassenger.utils.Result
import kotlinx.coroutines.delay
import retrofit2.HttpException
import timber.log.Timber
import javax.inject.Inject

class AppRepository @Inject constructor(
    private val apiService: ApiService,
    private val apiMidtransService: MidtransApiService
) {

    fun userRegis(
        name: String,
        email: String,
        age: Int,
        password: String
    ): LiveData<Result<RegisterPassengerResponse>> = liveData {
        emit(Result.Loading)
        val registerRequest = RegisterPassengerRequest(age, email, name, password)
        try {
            val response = apiService.register(registerRequest)
            emit(Result.Success(response))
        } catch (e: HttpException) {
            Timber.e(e)
            emit(Result.Error(e.message.toString()))
        }
    }

    fun userLogin(email: String, password: String): LiveData<Result<LoginResponse>> = liveData {
        emit(Result.Loading)
        val loginRequest = LoginRequest(email, password)
        try {
            val client = apiService.login(loginRequest)
            emit(Result.Success(client))
        } catch (e: HttpException) {
            Timber.e(e)
            emit(Result.Error(e.message.toString()))
        }
    }

    // TODO fetch from CC angkot apa saja
    fun findAngkotBaseOnPositionAndDestination(
        position: LatLng,
        destination: LatLng
    ): LiveData<Result<List<Angkot>>> = liveData {
        emit(Result.Loading)
        delay(500L)
        emit(Result.Success(Dummy.dummyAngkot()))
    }

    /*fun initGopay(midtransRequest: MidtransRequest): LiveData<Result<SanboxMidtransReponse>> =
        liveData {
            emit(Result.Loading)
            try {
                val data = apiMidtransService.initTransactionGopay(midtransRequest)
                emit(Result.Success(data))
            } catch (e : Exception) {
                Timber.e(e)
                emit(Result.Error(e.message.toString()))
            }
        }*/

    fun initTransaction(midtransRequest: MidtransRequest): LiveData<Result<SanboxMidtransReponse>> =
        liveData {
            emit(Result.Loading)
            try {
                val data = apiMidtransService.initTransactionShopee(midtransRequest)
                emit(Result.Success(data))
            } catch (e : HttpException) {
                Timber.e(e)
                emit(Result.Error(e.message.toString()))
            }
        }


    fun qrToMidtrans(qrRequest: QrRequest) : LiveData<Result<QrResponse>> = liveData {
        emit(Result.Loading)
        try {
            val data = apiMidtransService.qrToMidtrans(qrRequest)
            emit(Result.Success(data))
        } catch (e : HttpException) {
            Timber.e(e)
            emit(Result.Error(e.message.toString()))
        }
    }

    fun cancleMidtrans() : LiveData<Result<CancelResponse>>  = liveData {
        emit(Result.Loading)
        try {
            val data = apiMidtransService.midtransCancel()
            emit(Result.Success(data))
        } catch (e : HttpException) {
            Timber.e(e)
            emit(Result.Error(e.message.toString()))
        }
    }
}