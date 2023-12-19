package com.miftah.jakasforpassenger.core.data.source.remote.dto.request

data class MidtransRequest(
    val items: List<Item>,
    val payment_type: String
)

data class Item(
    val id: String,
    val name: String,
    val price: Int,
    val quantity: Int
)