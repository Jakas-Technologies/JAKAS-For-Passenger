package com.miftah.jakasforpassenger.core.data.source.remote.dto.request

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class QrRequest(
    val driverId : Int,
    val passengerId : Int,
    val tripCost : Double
) : Parcelable
