package com.miftah.jakasforpassenger.core.data.source.remote.dto.response

import com.google.gson.annotations.SerializedName

data class CancelResponse(

	@field:SerializedName("status")
	val status: String
)
