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
import com.example.cashiq.UI.activity.DashboardActivity
import com.example.cashiq.UI.activity.ForgetPasswordActivity
import com.example.cashiq.UI.activity.SignUpActivity
import com.example.cashiq.UI.activity.UserData
import com.example.cashiq.databinding.ActivityLoginBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Setting up the database
        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.reference.child("users")




        binding.buttonLogin.setOnClickListener {
            val email = binding.editTextEmail.text.toString()
            val password = binding.editTextPassword.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                if (isConnectedToInternet()) {
                    loginUser(email, password)
                } else {
                    showNoInternetError()
                }
            } else {
                Toast.makeText(this@LoginActivity, "All fields are mandatory", Toast.LENGTH_SHORT).show()
            }
        }


        binding.test.setOnClickListener{
            startActivity(Intent(this@LoginActivity, DashboardActivity::class.java))
        }







        binding.textViewForgotPassword.setOnClickListener {
            startActivity(Intent(this@LoginActivity, ForgetPasswordActivity::class.java))

        }

        binding.textViewSignUp.setOnClickListener {
            startActivity(Intent(this@LoginActivity, SignUpActivity::class.java))
            finish()
        }



        }

        binding.myConstraintLayout.setOnTouchListener { view, event ->
            // Hide the keyboard when touched anywhere on ConstraintLayout
            hideKeyboard(view)
            true  // Return true to indicate that the touch event was consumed
        }

        // Using OnBackPressedDispatcher to handle back press
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // If there's a currently focused view (keyboard is visible), hide the keyboard
                val currentFocusView = currentFocus
                if (currentFocusView != null) {
                    hideKeyboard(currentFocusView)
                } else {
                    // If no keyboard is visible, allow the default back press action (app closing)
                    isEnabled = false  // Disable this callback temporarily
                    onBackPressedDispatcher.onBackPressed()  // Use onBackPressedDispatcher to call back press behavior
                }
            }
        })
    }

        // validating Credentials

    private fun hideKeyboard(view: View) {
        val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val currentFocusView = currentFocus

        // If there's a currently focused view, hide the keyboard using its window token
        currentFocusView?.let {
            inputMethodManager.hideSoftInputFromWindow(it.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
        }
    }

//        signUpTextView.setOnClickListener {
//            // Redirect to Sign Up Activity
//            val intent = Intent(this, SignUpActivity::class.java)
//            startActivity(intent)
//        }
//    }

    // Function to check credentials in the database

    private fun loginUser(email: String, password: String) {
        databaseReference.orderByChild("email").equalTo(email)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        for (userSnapshot in snapshot.children) {
                            val userData = userSnapshot.getValue(UserData::class.java)
                            if (userData != null && userData.email == email && userData.password == password) {
                                Toast.makeText(
                                    this@LoginActivity,
                                    "Login Successful",
                                    Toast.LENGTH_SHORT
                                ).show()
                                startActivity(Intent(this@LoginActivity, DashboardActivity::class.java))
                                finish()
                                return
                            }
                        }
                    }
                    Toast.makeText(this@LoginActivity, "Login failed", Toast.LENGTH_SHORT).show()
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Toast.makeText(
                        this@LoginActivity,
                        "Database Error: ${databaseError.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }



    private fun isConnectedToInternet(): Boolean {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork
        val networkCapabilities = connectivityManager.getNetworkCapabilities(network)
        return networkCapabilities != null && networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    private fun showNoInternetError() {
        val builder = android.app.AlertDialog.Builder(this)
        builder.setTitle("No Internet Connection")
        builder.setMessage("Please check your internet connection and try again.")
        builder.setPositiveButton("OK") { dialog, _ ->
            dialog.dismiss() // Close the dialog
        }
        builder.setCancelable(false) // Prevent dismissing the dialog by tapping outside
        val dialog = builder.create()
        dialog.show()
    }

}
