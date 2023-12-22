package com.miftah.jakasforpassenger.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.miftah.jakasforpassenger.core.data.source.AppRepository
import com.miftah.jakasforpassenger.core.data.source.preference.UserPreference
import com.miftah.jakasforpassenger.core.data.source.preference.model.UserModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(private val repository: AppRepository) : ViewModel() {

    @Inject
    lateinit var userPreference: UserPreference

    fun createSave(id: Int, username: String, token: String, userType : String) {
        viewModelScope.launch {
            userPreference.saveSession(
                UserModel(
                    id = id,
                    username = username,
                    token = token,
                    userType = userType
                )
            )
        }
    }

    fun userRegis(email: String, username: String, password: String, age : Int) =
        repository.userRegis(name = username, email = email, password = password, age = age)

    fun userLogin(email: String, password: String) = repository.userLogin(email, password)
}