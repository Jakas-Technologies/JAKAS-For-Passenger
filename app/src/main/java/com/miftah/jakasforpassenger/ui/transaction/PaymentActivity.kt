package com.miftah.jakasforpassenger.ui.transaction

import android.content.Intent
import android.os.Bundle
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.miftah.jakasforpassenger.databinding.ActivityPaymentBinding
import com.miftah.jakasforpassenger.ui.home.MainActivity
import com.miftah.jakasforpassenger.utils.Constants
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class PaymentActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPaymentBinding
    private var loadUrl: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadUrl = intent.getStringExtra(Constants.EXTRA_URL_REDIRECT)
        Timber.d(loadUrl)
        loadUrl?.let {
            binding.sandboxPaymentGateway.settings.javaScriptEnabled = true
            binding.sandboxPaymentGateway.webViewClient = MyWebViewClient()
            binding.sandboxPaymentGateway.loadUrl(it)
        }
    }

    private inner class MyWebViewClient : WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
            val url: String = request.url.toString()

            if (url.startsWith("jakas")) {
                handleCallback(url)
                return true
            }

            view.loadUrl(url)
            return true
        }
    }

    private fun handleCallback(callbackUrl: String) {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }
}