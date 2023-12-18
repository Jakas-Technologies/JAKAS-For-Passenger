package com.miftah.jakasforpassenger.ui.transaction

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.miftah.jakasforpassenger.databinding.ActivityPaymentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PaymentActivity : AppCompatActivity() {

    private lateinit var binding : ActivityPaymentBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.sandboxPaymentGateway.loadUrl("http://www.dicoding.com")
    }
}