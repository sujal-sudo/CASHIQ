package com.example.cashiq

import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Email
import android.text.LoginFilter.UsernameFilterGeneric
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.cashiq.databinding.ActivityLoginBinding
import com.example.cashiq.databinding.ActivitySignUpBinding
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
            val username = binding.TextUser.text.toString()
            val email = binding.editTextEmail.text.toString()
            val password = binding.editTextPassword.text.toString()

            if (username.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
                loginUser(username, email, password)
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

    private fun loginUser(username: String, email: String, password: String){
        databaseReference.orderByChild("username").equalTo(username).addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    for(userSnapshot in snapshot.children) {
                        val userData = userSnapshot.getValue(UserData::class.java)

                        if(userData !=null && userData.email == email && userData.password==password){
                            Toast.makeText(this@LoginActivity, "Login Successful", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this@LoginActivity, Dashboard::class.java))
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
