package com.miftah.jakasforpassenger.core.data.source.preference

import com.miftah.jakasforpassenger.core.data.source.preference.model.UserModel
import kotlinx.coroutines.flow.Flow

interface UserPref {
    suspend fun saveSession(user: UserModel)

    fun getSession(): Flow<UserModel>

    suspend fun logout()
}