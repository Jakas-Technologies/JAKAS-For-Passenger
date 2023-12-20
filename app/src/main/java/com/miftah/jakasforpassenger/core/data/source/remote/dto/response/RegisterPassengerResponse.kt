package com.miftah.jakasforpassenger.core.data.source.remote.dto.response

import com.google.gson.annotations.SerializedName

data class RegisterPassengerResponse(

	@field:SerializedName("success")
	val success: Boolean,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("user")
	val user: User
)

data class User(

	@field:SerializedName("createdAt")
	val createdAt: String,

	@field:SerializedName("password")
	val password: String,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("userType")
	val userType: String,

	@field:SerializedName("accessToken")
	val accessToken: Any,

	@field:SerializedName("age")
	val age: Int,

	@field:SerializedName("email")
	val email: String,

	@field:SerializedName("updatedAt")
	val updatedAt: String?,

	@field:SerializedName("refreshToken")
	val refreshToken: String?
)
