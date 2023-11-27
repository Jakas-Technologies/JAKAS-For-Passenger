package com.miftah.jakasforpassenger.core.data.source.preference.model

data class UserModel(
    val username: String,
    val token: String,
    val isLogin: Boolean = false
)
