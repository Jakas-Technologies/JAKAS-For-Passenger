package com.miftah.jakasforpassenger.core.data.source.preference.model

data class UserModel(
    val id : Int,
    val username: String,
    val token: String,
    val userType : String,
    val isLogin: Boolean = false
)
