package com.example.cashiq.UI.activity

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.cashiq.R
import com.example.cashiq.databinding.ActivitySignUpBinding
import com.example.cashiq.model.UserData
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class SignUpActivity : AppCompatActivity() {


    // Firebase connection
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var binding: ActivitySignUpBinding



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivitySignUpBinding.inflate(layoutInflater)

//        setContentView(R.layout.activity_sign_up) // Ensure this layout exists
        setContentView(binding.root) // Ensure this layout exists


        //Firebase and Database
        firebaseDatabase = FirebaseDatabase.getInstance()// initializing database
        databaseReference = firebaseDatabase.reference.child("users")
        // here it will use database through databaserefrecnce
        binding.signupButton.setOnClickListener {
            val username = binding.signupName.text.toString()
            val email = binding.signupEmail.text.toString()
            val password = binding.signupPassword.text.toString()

            if (username.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
                if(isConnectedToInternet()){
                signupUser(username, email, password)}
                else(showNoInternetError())
            } else {
                Toast.makeText(this@SignUpActivity, "All fields are mandatory ", Toast.LENGTH_SHORT)
                    .show()
            }

        }
        binding.loginTextview.setOnClickListener {
            startActivity(Intent(this@SignUpActivity, LoginActivity::class.java))
            finish()
        }

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



        // Set click listener for the Login TextView
        loginTextview.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        // Set click listener for the Google Sign Up button
        googleSignupButton.setOnClickListener {
            signInWithGoogle()
        }

        // Set click listener for the Constraint layout for hiding keyboard
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
    // This is the database Fucntion that checks the required fucntion to run the database
    private fun signupUser(username: String, password: String, email: String){
        databaseReference.orderByChild("username").equalTo(username).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) { // snapshot is the object that specifies a particular location of a data
                if (!snapshot.exists()){
                    val id = databaseReference.push().key
                    val userData = UserData(id,username, email, password)
                    databaseReference.child(id!!).setValue(userData)
                    Toast.makeText(this@SignUpActivity, "Signup Successful" , Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@SignUpActivity,LoginActivity::class.java))
                    finish()
                } else{
                    Toast.makeText(this@SignUpActivity, "User Already Exists" , Toast.LENGTH_SHORT).show()
                }

            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(this@SignUpActivity, "Database Error: ${databaseError.message}" , Toast.LENGTH_SHORT).show()


            }
        })
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

    private fun hideKeyboard(view: View) {
        val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val currentFocusView = currentFocus

        // If there's a currently focused view, hide the keyboard using its window token
        currentFocusView?.let {
            inputMethodManager.hideSoftInputFromWindow(it.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
        }
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
