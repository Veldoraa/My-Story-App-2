package com.example.storyapp.ui.splash_screen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.example.storyapp.data.local.SharedPreferences
import com.example.storyapp.ui.login.LoginActivity
import com.example.storyapp.ui.mainstory.MainActivity
import com.example.storyapp.databinding.ActivitySplashScreenBinding

class SplashScreen : AppCompatActivity() {
    private lateinit var binding: ActivitySplashScreenBinding
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        SplashView()
    }

    private fun SplashView() {
        sharedPreferences = SharedPreferences(this)
        Handler(Looper.getMainLooper()).postDelayed({
            if (sharedPreferences.checkState()) {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }, 3000L)
    }
}