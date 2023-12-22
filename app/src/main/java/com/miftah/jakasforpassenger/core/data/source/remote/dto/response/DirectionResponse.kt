package com.miftah.jakasforpassenger.core.data.source.remote.dto.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class DirectionResponse(

	@field:SerializedName("success")
	val success: Boolean,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("drivers")
	val drivers: List<DriversItem>
)

@Parcelize
data class DriversItem(

	@field:SerializedName("createdAt")
	val createdAt: String,

	@field:SerializedName("password")
	val password: String,

	@field:SerializedName("licensePlate")
	val licensePlate: String,

	@field:SerializedName("routeId")
	val routeId: Int,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("accessToken")
	val accessToken: String?,

	@field:SerializedName("age")
	val age: Int,

	@field:SerializedName("email")
	val email: String,

	@field:SerializedName("routeName")
	val routeName: String,

	@field:SerializedName("refreshToken")
	val refreshToken: String?,

	@field:SerializedName("updatedAt")
	val updatedAt: String
) : Parcelable
