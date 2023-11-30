package com.miftah.jakasforpassenger.core.data.source

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.miftah.jakasforpassenger.core.data.source.remote.request.LoginRequest
import com.miftah.jakasforpassenger.core.data.source.remote.request.RegisterRequest
import com.miftah.jakasforpassenger.core.data.source.remote.response.LoginResponse
import com.miftah.jakasforpassenger.core.data.source.remote.response.RegisterResponse
import com.miftah.jakasforpassenger.core.data.source.remote.retrofit.ApiService
import com.miftah.jakasforpassenger.utils.Result
import retrofit2.HttpException
import timber.log.Timber
import javax.inject.Inject

class AppRepository @Inject constructor(
    private val apiService: ApiService
) {

    fun userRegis(
        name: String,
        email: String,
        password: String
    ): LiveData<Result<RegisterResponse>> = liveData {
        emit(Result.Loading)
        val registerRequest = RegisterRequest(name, email, password)
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


}