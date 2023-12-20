package com.miftah.jakasforpassenger.core.data.source.remote.dto.response

import com.google.gson.annotations.SerializedName

data class QrResponse(

	@field:SerializedName("trip")
	val trip: Trip,

	@field:SerializedName("status")
	val status: String
)

data class Trip(

	@field:SerializedName("isPaid")
	val isPaid: Boolean,

	@field:SerializedName("fare")
	val fare: Int,

	@field:SerializedName("createdAt")
	val createdAt: String,

	@field:SerializedName("onProgress")
	val onProgress: Boolean,

	@field:SerializedName("driverID")
	val driverID: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("userID")
	val userID: String,

	@field:SerializedName("updatedAt")
	val updatedAt: String
)
