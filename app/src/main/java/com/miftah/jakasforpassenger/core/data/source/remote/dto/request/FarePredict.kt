package com.miftah.jakasforpassenger.core.data.source.remote.dto.request

data class FarePredict(
    val destinationLat: Double,
    val destinationLng: Double,
    val originLat: Double,
    val originLng: Double,
    val passengerType: String
)