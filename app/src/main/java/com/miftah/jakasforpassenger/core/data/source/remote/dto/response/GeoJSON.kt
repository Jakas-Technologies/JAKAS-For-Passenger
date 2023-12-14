package com.miftah.jakasforpassenger.core.data.source.remote.dto.response

import com.google.gson.annotations.SerializedName


data class GeoJSON(

	@field:SerializedName("geometry")
	val geometry: Geometry,

	@field:SerializedName("type")
	val type: String,

	@field:SerializedName("properties")
	val properties: Properties
)

data class Geometry(

	@field:SerializedName("coordinates")
	val coordinates: List<Double>,

	@field:SerializedName("type")
	val type: String
)

data class Properties(

	@field:SerializedName("name")
	val name: String
)
