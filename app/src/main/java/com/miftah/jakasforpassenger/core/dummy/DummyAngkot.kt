package com.miftah.jakasforpassenger.core.dummy

object Dummy {
    fun dummyAngkot(): List<Angkot> {
        val angkots = mutableListOf<Angkot>()
        for (i in 1..10) {
            angkots.add(
                Angkot(
                    department = "$i",
                    price = i
                )
            )
        }
        return angkots
    }
}