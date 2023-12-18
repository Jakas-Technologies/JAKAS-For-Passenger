package com.miftah.jakasforpassenger.ui.qrscanner

interface QRCodeListener {
    fun onQRCodeScanned(rawValue: String)
}