package com.example.cashiq

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task

class SignUpActivity : AppCompatActivity() {

    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up) // Ensure this layout exists

        // Initialize UI elements
        val signupButton: Button = findViewById(R.id.signup_button)
        val googleSignupButton: Button = findViewById(R.id.google_signup_button)
        val termsCheckbox: CheckBox = findViewById(R.id.terms_checkbox)
        val emailEditText: EditText = findViewById(R.id.signup_email)
        val nameEditText: EditText = findViewById(R.id.signup_name)
        val passwordEditText: EditText = findViewById(R.id.signup_password)
        val loginTextview: TextView = findViewById(R.id.loginTextview)

        // Configure Google Sign-In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        // Set click listener for the Sign Up button
        signupButton.setOnClickListener {
            val name = nameEditText.text.toString()
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            // Check if the checkbox is checked
            if (termsCheckbox.isChecked) {
                Toast.makeText(this, "Signing up with $email", Toast.LENGTH_SHORT).show()
                // Add your sign-up logic here (e.g., validation, API call)
            } else {
                Toast.makeText(this, "Please agree to the terms", Toast.LENGTH_SHORT).show()
            }
        }

        // Set click listener for the Login TextView
        loginTextview.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        // Set click listener for the Google Sign Up button
        googleSignupButton.setOnClickListener {
            signInWithGoogle()
        }
    }

    private val signInLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        handleSignInResult(task)
    }

    private fun signInWithGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        signInLauncher.launch(signInIntent)
    }

    private fun handleSignInResult(task: Task<GoogleSignInAccount>) {
        try {
            val account = task.getResult(Exception::class.java)
            account?.let {
                Log.d("GoogleSignIn", "Sign-in successful. Name: ${it.displayName}, Email: ${it.email}")
                Toast.makeText(this, "Signed in as: ${it.email}", Toast.LENGTH_SHORT).show()
                // Add logic for successful sign-in (e.g., navigate to the main activity)
            }
        } catch (e: Exception) {
            Log.e("GoogleSignIn", "Sign-in failed", e)
            Toast.makeText(this, "Sign-in failed.", Toast.LENGTH_SHORT).show()
        }
    }
}
