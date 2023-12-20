package com.miftah.jakasforpassenger.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.miftah.jakasforpassenger.core.data.source.AppRepository
import com.miftah.jakasforpassenger.core.data.source.preference.UserPref
import com.miftah.jakasforpassenger.core.data.source.preference.model.UserModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(private val repository: AppRepository) : ViewModel() {

    @Inject
    lateinit var userPref: UserPref

    fun createSave(username : String) {
        viewModelScope.launch {
            userPref.saveSession(
                UserModel(
                    username = username,
                    token = "123",
                )
            )
        }
    }
    fun userRegis(email: String, username: String, password: String, age : Int) =
        repository.userRegis(name = username, email = email, password = password, age = age)

    fun userLogin(email: String, password: String) = repository.userLogin(email, password)
}