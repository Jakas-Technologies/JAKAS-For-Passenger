package com.miftah.jakasforpassenger.utils

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SerializableDestination(
    var name: String?,
    var address: String?,
    var latitude: Double,
    var longitude: Double
) : Parcelable

@Parcelize
data class Angkot(
    val id : Int,
    val department: String,
    val price : Int
) : Parcelable
