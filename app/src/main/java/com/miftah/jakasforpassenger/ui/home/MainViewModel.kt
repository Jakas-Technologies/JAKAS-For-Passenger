package com.miftah.jakasforpassenger.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.miftah.jakasforpassenger.core.data.source.AppRepository
import com.miftah.jakasforpassenger.core.data.source.preference.UserPreference
import com.miftah.jakasforpassenger.core.data.source.preference.model.UserModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: AppRepository) : ViewModel() {

    @Inject
    lateinit var userPreference: UserPreference

    fun getSession(): LiveData<UserModel> = userPreference.getSession().asLiveData()

    fun logOut(id : Int) = repository.logout(id)

    fun getIdUser() = runBlocking {
        userPreference.getSession().first().id
    }

    fun removeSession() {
        viewModelScope.launch {
            userPreference.logout()
        }
    }

}