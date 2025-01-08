package com.example.cashiq.UI.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.cashiq.R
import com.example.cashiq.databinding.ActivityLoginBinding
import com.example.cashiq.databinding.ActivitySignUpBinding
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
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)


        //Firebase and Database
        firebaseDatabase = FirebaseDatabase.getInstance()// initializing database
        databaseReference = firebaseDatabase.reference.child("users")
        // here it will use database through databaserefrecnce
        binding.signupButton.setOnClickListener {
            val username = binding.signupName.text.toString()
            val email = binding.signupEmail.text.toString()
            val password = binding.signupPassword.text.toString()

            if (username.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
                signupUser(username, email, password)
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

//        // Set click listener for the Sign Up button
//        signupButton.setOnClickListener {
//            val name = nameEditText.text.toString()
//            val email = emailEditText.text.toString()
//            val password = passwordEditText.text.toString()
//
//            // Check if the checkbox is checked
//            if (termsCheckbox.isChecked) {
//                Toast.makeText(this, "Signing up with $email", Toast.LENGTH_SHORT).show()
//                // Add your sign-up logic here (e.g., validation, API call)
//            } else {
//                Toast.makeText(this, "Please agree to the terms", Toast.LENGTH_SHORT).show()
//            }
//        }

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
}
