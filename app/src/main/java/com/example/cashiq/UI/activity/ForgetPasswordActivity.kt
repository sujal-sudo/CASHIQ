package com.example.cashiq.UI.activity

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.cashiq.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidUserException

class ForgetPasswordActivity : AppCompatActivity() {

    private lateinit var constraintLayout: ConstraintLayout
    private lateinit var emailEditText: EditText
    private lateinit var resetPasswordButton: Button
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_forget_password)

        initializeViews()
        setOnClickListeners()

        firebaseAuth = FirebaseAuth.getInstance()

        // Handle back button behavior
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val currentFocusView = currentFocus
                if (currentFocusView != null) {
                    hideKeyboard(currentFocusView)
                } else {
                    isEnabled = false
                    onBackPressedDispatcher.onBackPressed()
                }
            }
        })
    }

    private fun initializeViews() {
        constraintLayout = findViewById(R.id.myConstraintLayout)
        emailEditText = findViewById(R.id.email_forgot)
        resetPasswordButton = findViewById(R.id.EmailCode_forgot)
    }

    private fun setOnClickListeners() {
        // Hide keyboard when touching outside
        constraintLayout.setOnTouchListener { view, _ ->
            hideKeyboard(view)
            true
        }

        // Handle Reset Password Button Click
        resetPasswordButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()

            if (email.isEmpty()) {
                Toast.makeText(this, "Please enter your email", Toast.LENGTH_SHORT).show()
            } else {
                resetPassword(email)
            }
        }
    }

    private fun resetPassword(email: String) {
        firebaseAuth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Reset link sent to your email!", Toast.LENGTH_LONG).show()
                } else {
                    try {
                        throw task.exception ?: Exception("Unknown error")
                    } catch (e: FirebaseAuthInvalidUserException) {
                        Toast.makeText(this, "This email is not registered!", Toast.LENGTH_LONG).show()
                    } catch (e: Exception) {
                        Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                    }
                }
            }
    }

    private fun hideKeyboard(view: View) {
        val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        currentFocus?.let {
            inputMethodManager.hideSoftInputFromWindow(it.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
        }
    }
}
