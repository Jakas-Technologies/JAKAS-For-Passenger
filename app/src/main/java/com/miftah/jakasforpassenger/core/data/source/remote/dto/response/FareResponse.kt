package com.miftah.jakasforpassenger.core.data.source.remote.dto.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class FareResponse(

	@field:SerializedName("data")
	val data: Data,

	@field:SerializedName("status")
	val status: String
): Parcelable

@Parcelize
data class Data(

	@field:SerializedName("fare")
	val fare: Int,

	@field:SerializedName("passengerType")
	val passengerType: String,

	@field:SerializedName("distance")
	val distance: Int,

	@field:SerializedName("fuelPrice")
	val fuelPrice: Int
): Parcelable
