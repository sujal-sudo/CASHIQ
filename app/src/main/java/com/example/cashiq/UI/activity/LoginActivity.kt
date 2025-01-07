package com.example.cashiq.UI.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
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

        // Setting Up database
        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.reference.child("users")

        binding.buttonLogin.setOnClickListener{

            val email = binding.editTextEmail.text.toString()
            val password = binding.editTextPassword.text.toString()

            if ( email.isNotEmpty() && password.isNotEmpty()) {
                loginUser( email, password)
            } else {
                Toast.makeText(this@LoginActivity, "All fields are mandatory ", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        binding.textViewSignUp.setOnClickListener{
            startActivity(Intent(this@LoginActivity, SignUpActivity::class.java))
            finish()
        }







//        val loginButton: Button = findViewById(R.id.buttonLogin)
//        val signUpTextView: TextView = findViewById(R.id.textViewSignUp) // Reference to Sign Up TextView
//
//        //Get from the login elements
//        val etEmail = findViewById<EditText>(R.id.editTextEmail)
//        val etPassword = findViewById<EditText>(R.id.editTextPassword)

//
//        loginButton.setOnClickListener {
//            // Handle login logic here
//            val email = etEmail.text.toString()
//            val password = etPassword.text.toString()

        // validating Credentials


    }

//        signUpTextView.setOnClickListener {
//            // Redirect to Sign Up Activity
//            val intent = Intent(this, SignUpActivity::class.java)
//            startActivity(intent)
//        }
//    }

    // Function to check credentials in the database

    private fun loginUser(email: String, password: String){
        databaseReference.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    for(userSnapshot in snapshot.children) {
                        val userData = userSnapshot.getValue(UserData::class.java)

                        if(userData !=null && userData.email == email && userData.password==password){
                            Toast.makeText(this@LoginActivity, "Login Successful", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this@LoginActivity, DashboardActivity::class.java))
                            finish()
                            return
                        }

                    }
                }
                Toast.makeText(this@LoginActivity, "Login failed", Toast.LENGTH_SHORT).show()

            }

            override fun onCancelled(databaseError : DatabaseError) {
                Toast.makeText(this@LoginActivity, "Database Error: ${databaseError.message}" , Toast.LENGTH_SHORT).show()

            }
        } )
    }
}