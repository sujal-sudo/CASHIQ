package com.example.cashiq.repository

import com.google.firebase.auth.FirebaseAuth

class AuthRepoImpl(private val auth: FirebaseAuth = FirebaseAuth.getInstance()) : AuthRepo {

    override fun login(email: String, password: String, callback: (Boolean, String) -> Unit) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                callback(true, "Login successful.")
            } else {
                callback(false, task.exception?.message ?: "Login failed.")
            }
        }
    }

    override fun register(email: String, password: String, callback: (Boolean, String) -> Unit) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                callback(true, "Registration successful.")
            } else {
                callback(false, task.exception?.message ?: "Registration failed.")
            }
        }
    }

    override fun logout() {
        auth.signOut()
    }

    override fun getCurrentUserId(): String? {
        return auth.currentUser?.uid
    }
}