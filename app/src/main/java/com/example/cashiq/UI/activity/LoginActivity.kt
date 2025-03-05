package com.example.cashiq.UI.activity

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.example.cashiq.R
import com.example.cashiq.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        // **Check if user is already logged in and redirect to Dashboard**
        if (isUserLoggedIn()) {
            startActivity(Intent(this, DashboardActivity::class.java))
            finish()
        }

        // Login Button Click Listener
        binding.buttonLogin.setOnClickListener {
            val email = binding.editTextEmail.text.toString().trim()
            val password = binding.editTextPassword.text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                if (isConnectedToInternet()) {
                    loginUser(email, password)
                } else {
                    showNoInternetError()
                }
            } else {
                showError("All fields are mandatory")
            }
        }

        // Navigation to SignUp
        binding.textViewSignUp.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }


        // Hide Keyboard when tapping outside
        binding.myConstraintLayout.setOnTouchListener { _, _ ->
            hideKeyboard()
            true
        }

        // Handle Back Press
        handleBackPress()
    }

    private fun loginUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    saveUserSession()
                    showToast("Login Successful!")
                    startActivity(Intent(this, DashboardActivity::class.java))
                    finish()
                } else {
                    showError("Login failed: ${task.exception?.message}")
                }
            }
    }

    private fun saveUserSession() {
        val sharedPreferences = getSharedPreferences("UserSession", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean("isLoggedIn", true)
        editor.putLong("lastLoginTime", System.currentTimeMillis()) // Save login timestamp
        editor.apply()
    }

    private fun isUserLoggedIn(): Boolean {
        val sharedPreferences = getSharedPreferences("UserSession", Context.MODE_PRIVATE)
        val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)
        val lastLoginTime = sharedPreferences.getLong("lastLoginTime", 0)

        // Optional: Auto logout after 24 hours (86400000 milliseconds)
        val sessionTimeout = 86400000L
        if (isLoggedIn && System.currentTimeMillis() - lastLoginTime < sessionTimeout && auth.currentUser != null) {
            return true
        }

        logoutUser() // Auto logout if session expired
        return false
    }

    private fun logoutUser() {
        auth.signOut()

        val sharedPreferences = getSharedPreferences("UserSession", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
    }

    private fun isConnectedToInternet(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork
        val networkCapabilities = connectivityManager.getNetworkCapabilities(network)
        return networkCapabilities != null && networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    private fun showNoInternetError() {
        showError("No Internet Connection. Please check your connection and try again.")
    }

    private fun hideKeyboard() {
        val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val currentFocusView = currentFocus
        currentFocusView?.let {
            inputMethodManager.hideSoftInputFromWindow(it.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
        }
    }

    private fun handleBackPress() {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (currentFocus != null) {
                    hideKeyboard()
                } else {
                    isEnabled = false
                    onBackPressedDispatcher.onBackPressed()
                }
            }
        })
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showError(message: String) {
        val toast = Toast.makeText(this, message, Toast.LENGTH_LONG)
        toast.view?.setBackgroundResource(R.drawable.error_toast_background) // Custom background
        toast.show()
    }
}
