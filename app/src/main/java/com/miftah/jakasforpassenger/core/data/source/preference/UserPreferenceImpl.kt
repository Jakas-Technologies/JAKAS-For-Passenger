package com.miftah.jakasforpassenger.core.data.source.preference

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.miftah.jakasforpassenger.core.data.source.preference.model.UserModel
import com.miftah.jakasforpassenger.utils.Constants.IS_LOGIN_KEY
import com.miftah.jakasforpassenger.utils.Constants.SHARED_PREFERENCES_KEY
import com.miftah.jakasforpassenger.utils.Constants.TOKEN_KEY
import com.miftah.jakasforpassenger.utils.Constants.USERNAME
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = SHARED_PREFERENCES_KEY)

class UserPreferenceImpl @Inject constructor(val dataStore: DataStore<Preferences>) : UserPref {

    override suspend fun saveSession(user: UserModel) {
        dataStore.edit { preferences ->
            preferences[TOKEN_KEY] = user.token
            preferences[USERNAME] = user.username
            preferences[IS_LOGIN_KEY] = true
        }
    }

    override fun getSession(): Flow<UserModel> {
        return dataStore.data.map { preferences ->
            UserModel(
                preferences[USERNAME] ?: "",
                preferences[TOKEN_KEY] ?: "",
                preferences[IS_LOGIN_KEY] ?: false
            )
        }
    }

    override suspend fun logout() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}