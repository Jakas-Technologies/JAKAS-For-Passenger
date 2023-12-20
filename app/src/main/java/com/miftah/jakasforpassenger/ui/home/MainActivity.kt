package com.miftah.jakasforpassenger.ui.home

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.miftah.jakasforpassenger.R
import com.miftah.jakasforpassenger.databinding.ActivityMainBinding
import com.miftah.jakasforpassenger.ui.auth.OnboardingActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding

    private val viewModel: MainViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (intent?.action == Intent.ACTION_VIEW) {
            val deeplink = intent.data
            val host : String? = deeplink?.host

            if("callback" == host) {
                Toast.makeText(this, "berhasil", Toast.LENGTH_SHORT).show()
            }
        }

        setupBottomNav()

        viewModel.getSession().observe(this) {
            if (!it.isLogin) {
                startActivity(Intent(this, OnboardingActivity::class.java))
                finish()
            }
        }
    }

    private fun setupBottomNav() {
        val navController = findNavController(R.id.fragment_container_main)
        binding.bottomNavigationView.setupWithNavController(navController)
    }
}