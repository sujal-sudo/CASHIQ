package com.example.cashiq.UI.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.cashiq.R

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val loginButton: Button = findViewById(R.id.buttonLogin)
        val signUpTextView: TextView = findViewById(R.id.textViewSignUp) // Reference to Sign Up TextView

        loginButton.setOnClickListener {
            // Handle login logic here
        }

        signUpTextView.setOnClickListener {
            // Redirect to Sign Up Activity
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
    }
}