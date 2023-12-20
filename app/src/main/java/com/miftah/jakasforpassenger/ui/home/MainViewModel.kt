package com.miftah.jakasforpassenger.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.miftah.jakasforpassenger.core.data.source.AppRepository
import com.miftah.jakasforpassenger.core.data.source.preference.UserPref
import com.miftah.jakasforpassenger.core.data.source.preference.model.UserModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: AppRepository) : ViewModel() {

    @Inject
    lateinit var userPref: UserPref

    fun getSession(): LiveData<UserModel> = userPref.getSession().asLiveData()

    fun removeSession() {
        viewModelScope.launch {
            userPref.logout()
        }
    }
}