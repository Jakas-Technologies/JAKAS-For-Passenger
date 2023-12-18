package com.miftah.jakasforpassenger.ui.qrscanner

import android.os.Bundle
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import com.miftah.jakasforpassenger.databinding.ActivityQrCodeScannerBinding
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.util.concurrent.Executors

@AndroidEntryPoint
class QrCodeScannerActivity : AppCompatActivity() {

    private lateinit var cameraProvider: ProcessCameraProvider
    private lateinit var qrCodeScanner: QRCodeScanner
    private lateinit var resultTextView: TextView

    private lateinit var binding: ActivityQrCodeScannerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQrCodeScannerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        requestPermission()
    }

    private fun startCamera() {
        val preview = Preview.Builder().build()
        val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

        qrCodeScanner = QRCodeScanner(object : QRCodeListener {
            override fun onQRCodeScanned(rawValue: String) {
                resultTextView.text = rawValue
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
}