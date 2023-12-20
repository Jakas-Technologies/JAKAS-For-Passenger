package com.miftah.jakasforpassenger.core.data.source.remote.dto.request

data class RegisterPassengerRequest(
    val age: Int,
    val email: String,
    val name: String,
    val password: String
)