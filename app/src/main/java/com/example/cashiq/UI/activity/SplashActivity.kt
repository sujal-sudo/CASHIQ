package com.example.cashiq.UI.activity

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import android.view.animation.AccelerateDecelerateInterpolator
import com.example.cashiq.R
import com.google.firebase.auth.FirebaseAuth

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

        // Delay for animation, then check session and navigate accordingly
        logoText.postDelayed({
            checkUserSession()
        }, 1500)
    }

    private fun checkUserSession() {
        if (isUserLoggedIn()) {
            // Redirect to Dashboard if already logged in
            startActivity(Intent(this, DashboardActivity::class.java))
        } else {
            // Redirect to Login if not logged in
            startActivity(Intent(this, OnboardingActivity::class.java))
        }
        finish()
    }

    private fun isUserLoggedIn(): Boolean {
        val sharedPreferences = getSharedPreferences("UserSession", Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean("isLoggedIn", false) && FirebaseAuth.getInstance().currentUser != null
    }
}
