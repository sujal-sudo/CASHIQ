package com.example.cashiq

import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Email
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val loginButton: Button = findViewById(R.id.buttonLogin)
        val signUpTextView: TextView = findViewById(R.id.textViewSignUp) // Reference to Sign Up TextView

        //Get from the login elements
        val etEmail = findViewById<EditText>(R.id.editTextEmail)
        val etPassword = findViewById<EditText>(R.id.editTextPassword)


        loginButton.setOnClickListener {
            // Handle login logic here
            val email = etEmail.text.toString()
            val password = etPassword.text.toString()

            // validating Credentials
            

        }

        signUpTextView.setOnClickListener {
            // Redirect to Sign Up Activity
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
    }
}
