package com.miftah.jakasforpassenger.utils

import android.os.Parcelable
import com.google.android.gms.maps.model.LatLng
import kotlinx.parcelize.Parcelize

@Parcelize
data class SerializableDestination(
    var name: String?,
    var address: String?,
    var latLng: LatLng
) : Parcelable

@Parcelize
data class Angkot(
    val id : Int,
    val department: String,
    val price : Int
) : Parcelable
