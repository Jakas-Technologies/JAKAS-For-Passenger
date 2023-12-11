package com.miftah.jakasforpassenger.utils

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SerializableLatLng(
    val latitude: Double,
    val longitude: Double
) : Parcelable

@Parcelize
data class Angkot(
    val department: String,
    val price : Int
) : Parcelable
