package com.miftah.jakasforpassenger.feature.auth

import androidx.lifecycle.ViewModel
import com.miftah.jakasforpassenger.core.data.source.AppRepository

class OnboardingViewModel(private val repository : AppRepository) : ViewModel() {
    fun userRegis(email: String, username: String, password: String) =
        repository.userRegis(name = username, email = email, password = password)

//    fun userLogin(email: String, password: String) = repository.userLogin(email, password)
}