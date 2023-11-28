package com.miftah.jakasforpassenger.core.data.source

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.google.gson.Gson
import com.miftah.jakasforpassenger.core.data.source.preference.UserPreference
import com.miftah.jakasforpassenger.core.data.source.remote.request.RegisterRequest
import com.miftah.jakasforpassenger.core.data.source.remote.response.RegisterResponse
import com.miftah.jakasforpassenger.core.data.source.remote.retrofit.ApiService
import retrofit2.HttpException

class AppRepository(
    private val apiService: ApiService,
    private val userPreference: UserPreference,
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
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, RegisterResponse::class.java)
            val errorMessage = errorBody.message
            Log.d(TAG, "userLogin: $errorMessage")
            emit(Result.Error(errorMessage))
        }
    }


    companion object {
        const val TAG = "AppRepositoryLog"
    }
}