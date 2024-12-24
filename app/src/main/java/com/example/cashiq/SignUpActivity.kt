package com.example.cashiq

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.gcm.Task


    class SignUpActivity : AppCompatActivity() {
//        private lateinit var googleSignInClient: GoogleSignInClient
//        private val RC_SIGN_IN = 9001 // Request code for Google sign-in


        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_sign_up)  // Make sure this layout exists

            // Initialize UI elements
            val signupButton: Button = findViewById(R.id.signup_button)
            val googleSignupButton: Button = findViewById(R.id.google_signup_button)
            val termsCheckbox: CheckBox = findViewById(R.id.terms_checkbox)
            val emailEditText: EditText = findViewById(R.id.signup_email)
            val nameEditText: EditText = findViewById(R.id.signup_name)
            val passwordEditText: EditText = findViewById(R.id.signup_password)
            val loginTextview: TextView = findViewById(R.id.loginTextview)

//
//        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//            .requestEmail()
//            .build()
//        googleSignInClient = GoogleSignIn.getClient(this, gso)

            // Set click listener for the Sign Up button
            signupButton.setOnClickListener {
                val name = nameEditText.text.toString()
                val email = emailEditText.text.toString()
                val password = passwordEditText.text.toString()

                // Check if the checkbox is checked
                if (termsCheckbox.isChecked) {
                    // Handle normal sign up
                    Toast.makeText(this, "Signing up with $email", Toast.LENGTH_SHORT).show()
                    // Add your sign-up logic here (e.g., validation, API call)
                } else {
                    Toast.makeText(this, "Please agree to the terms", Toast.LENGTH_SHORT).show()
                }
            }
            loginTextview.setOnClickListener {
                // Redirect to Sign Up Activity
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }

//        // Set click listener for the Google Sign Up button
//        googleSignupButton.setOnClickListener {
//            signInWithGoogle()
//        }
//    }

//    private fun signInWithGoogle() {
//        val signInIntent: Intent = googleSignInClient.signInIntent
//        startActivityForResult(signInIntent, RC_SIGN_IN)
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//
//        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
//        if (requestCode == RC_SIGN_IN) {
//            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
//            handleSignInResult(task)
//        }
//    }
//
//    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
//        try {
//            val account: GoogleSignInAccount = completedTask.getResult(ApiException::class.java)
//            // Signed in successfully, show authenticated UI.
//            Toast.makeText(this, "Signed in as: ${account.email}", Toast.LENGTH_SHORT).show()
//            // Here you can add logic to handle the signed-in user (e.g., navigate to the main activity)
//        } catch (e: ApiException) {
//            Log.w("Google Sign-In", "signInResult:failed code=${e.statusCode}")
//            Toast.makeText(this, "Sign in failed.", Toast.LENGTH_SHORT).show()
//        }
//    }
        }
    }