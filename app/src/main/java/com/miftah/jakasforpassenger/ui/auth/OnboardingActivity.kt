package com.miftah.jakasforpassenger.ui.auth

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.miftah.jakasforpassenger.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnboardingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding)
    }
}