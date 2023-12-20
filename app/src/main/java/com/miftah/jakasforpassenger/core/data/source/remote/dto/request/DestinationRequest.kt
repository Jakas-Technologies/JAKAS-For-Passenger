package com.miftah.jakasforpassenger.core.data.source.remote.dto.request

data class DestinationRequest(
    val destinationLat: Double,
    val destinationLng: Double,
    val originLat: Double,
    val originLng: Double,
    val passengerType: String
)