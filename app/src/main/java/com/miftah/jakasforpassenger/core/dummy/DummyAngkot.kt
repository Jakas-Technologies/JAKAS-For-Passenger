package com.miftah.jakasforpassenger.core.dummy

import com.miftah.jakasforpassenger.utils.Angkot

object Dummy {
    fun dummyAngkot(): List<Angkot> {
        val angkots = mutableListOf<Angkot>()
        for (i in 1..10) {
            angkots.add(
                Angkot(
                    department = "$i",
                    price = i * 1000
                )
            )
        }
        return angkots
    }
}