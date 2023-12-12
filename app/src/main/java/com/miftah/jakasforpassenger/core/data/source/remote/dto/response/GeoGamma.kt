package com.miftah.jakasforpassenger.core.data.source.remote.dto.response

import com.google.gson.annotations.SerializedName

data class GeoGamma(

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("coords")
	val coords: Coords
)

data class Coords(

	@field:SerializedName("altitude")
	val altitude: Double,

	@field:SerializedName("heading")
	val heading: Double,

	@field:SerializedName("latitude")
	val latitude: Double,

	@field:SerializedName("accuracy")
	val accuracy: Double,

	@field:SerializedName("altitudeAccuracy")
	val altitudeAccuracy: Double,

	@field:SerializedName("speed")
	val speed: Double,

	@field:SerializedName("longitude")
	val longitude: Double
)
