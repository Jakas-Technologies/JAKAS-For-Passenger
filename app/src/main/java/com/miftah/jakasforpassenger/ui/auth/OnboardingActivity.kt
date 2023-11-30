package com.miftah.jakasforpassenger.ui.auth

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.miftah.jakasforpassenger.R
import dagger.hilt.android.AndroidEntryPoint
import com.miftah.jakasforpassenger.databinding.ActivityOnboardingBinding

@AndroidEntryPoint
class OnboardingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOnboardingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}