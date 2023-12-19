package com.miftah.jakasforpassenger.ui.transaction

import android.os.Bundle
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.miftah.jakasforpassenger.databinding.ActivityPaymentBinding
import com.miftah.jakasforpassenger.utils.Constants
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PaymentActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPaymentBinding
    private var loadUrl: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadUrl = intent.getStringExtra(Constants.EXTRA_DEPARTMENT_ANGKOT)

        loadUrl?.let {
            binding.sandboxPaymentGateway.settings.javaScriptEnabled = true
            binding.sandboxPaymentGateway.loadUrl(it)

            binding.sandboxPaymentGateway.webViewClient = object : WebViewClient() {
                
            }
        }
    }
}