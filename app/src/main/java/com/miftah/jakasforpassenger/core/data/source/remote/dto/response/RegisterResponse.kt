package com.miftah.jakasforpassenger.core.data.source.remote.dto.response

import com.google.gson.annotations.SerializedName

data class RegisterResponse(

	@field:SerializedName("code")
	val code: String,

	@field:SerializedName("message")
	val message: String
)
