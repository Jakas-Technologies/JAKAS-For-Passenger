package com.miftah.jakasforpassenger.ui.qrscanner

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.google.gson.Gson
import com.miftah.jakasforpassenger.databinding.ActivityQrCodeScannerBinding
import com.miftah.jakasforpassenger.utils.Constants
import com.miftah.jakasforpassenger.utils.QrScanning
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.io.IOException
import java.util.concurrent.Executors

@AndroidEntryPoint
class QrCodeScannerActivity : AppCompatActivity() {

    private lateinit var cameraProvider: ProcessCameraProvider
    private lateinit var qrCodeScanner: QRCodeScanner

    private lateinit var binding: ActivityQrCodeScannerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQrCodeScannerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            cameraProvider = cameraProviderFuture.get()
            startCamera()
        }, ContextCompat.getMainExecutor(this))

        requestPermission()
    }

    private fun startCamera() {
        val preview = Preview.Builder().build()
        val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

        qrCodeScanner = QRCodeScanner(object : QRCodeListener {
            override fun onQRCodeScanned(rawValue: String) {
                parsingToObject(rawValue)
            }
        })

        val imageAnalysis = ImageAnalysis.Builder()
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build()
            .apply {
                setAnalyzer(Executors.newSingleThreadExecutor(), qrCodeScanner)
            }

        try {
            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageAnalysis)
            preview.setSurfaceProvider(binding.previewView.surfaceProvider)
        } catch (exc: Exception) {
            Timber.e(exc)
        }
    }

    private fun requestPermission() {
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                startCamera()
            } else {
                finish()
            }
        }.launch(android.Manifest.permission.CAMERA)
    }

    private fun parsingToObject(newValue: String) {
        val gson = Gson()
        val intent = Intent()

        try {
            val dataJson = gson.fromJson(newValue, QrScanning::class.java)
            dataJson.let {
                it.id?: throw IOException("Qr Code is not valid")
                it.name?:throw IOException("Qr Code is not valid")
                it.licensePlate?:throw IOException("Qr Code is not valid")
                it.routeId?:throw IOException("Qr Code is not valid")
                it.routeName?:throw IOException("Qr Code is not valid")
            }
            val dataIntent = intent.putExtra(Constants.EXTRA_QR_CODE, dataJson)
            setResult(RESULT_OK, dataIntent)
            cameraProvider.unbindAll()
            finish()
        } catch (e : Exception) {
            Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroy() {
        cameraProvider.unbindAll()
        super.onDestroy()
    }
}