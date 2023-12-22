package com.miftah.jakasforpassenger.core.data.source.remote.dto.response

import com.google.gson.annotations.SerializedName

data class FareResponse(

	@field:SerializedName("data")
	val data: Data,

	@field:SerializedName("status")
	val status: String
)

data class Data(

	@field:SerializedName("fare")
	val fare: Int,

	@field:SerializedName("passengerType")
	val passengerType: String,

	@field:SerializedName("distance")
	val distance: Int,

	@field:SerializedName("fuelPrice")
	val fuelPrice: Int
)
