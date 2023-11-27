package com.miftah.jakasforpassenger.core.data.source

import com.miftah.jakasforpassenger.core.data.source.preference.UserPreference
import com.miftah.jakasforpassenger.core.data.source.remote.retrofit.ApiService

class AppRepository(
    private val apiService: ApiService,
    private val userPreference: UserPreference,
) {

}