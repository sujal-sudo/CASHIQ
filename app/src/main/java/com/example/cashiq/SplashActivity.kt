// SplashActivity.kt
package com.example.cashiq

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import android.view.animation.AccelerateDecelerateInterpolator

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)


        val logoText: TextView = findViewById(R.id.logotext)
        // Create fade in and scale animations
        val fadeIn = ObjectAnimator.ofFloat(logoText, "alpha", 0f, 1f)
        val scaleX = ObjectAnimator.ofFloat(logoText, "scaleX", 0.5f, 1f)
        val scaleY = ObjectAnimator.ofFloat(logoText, "scaleY", 0.5f, 1f)

        // Combine animations
        AnimatorSet().apply {
            playTogether(fadeIn, scaleX, scaleY)
            duration = 1500 // 1.5 seconds
            interpolator = AccelerateDecelerateInterpolator()
            start()
        }

        // Navigate to onboarding after animation
        logoText.postDelayed({
            startActivity(Intent(this, OnboardingActivity::class.java))
            finish()
        }, 1000)
    }
}