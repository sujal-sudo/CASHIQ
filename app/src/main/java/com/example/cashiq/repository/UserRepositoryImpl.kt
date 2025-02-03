package com.example.cashiq.repository

import com.example.cashiq.model.UserData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*

class UserRepositoryImpl : UserRepository {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val ref: DatabaseReference = database.reference.child("users")

    override fun loginUser(email: String, password: String, callback: (Boolean, String) -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    callback(true, "Login success")
                } else {
                    callback(false, task.exception?.message ?: "Login failed")
                }
            }
    }

    override fun signupUser(email: String, password: String, callback: (Boolean, String, String) -> Unit) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    callback(true, "Registration success", auth.currentUser?.uid ?: "")
                } else {
                    callback(false, task.exception?.message ?: "Signup failed", "")
                }
            }
    }

    override fun addUserToDatabase(id: String, userData: UserData, callback: (Boolean, String) -> Unit) {
        ref.child(id).setValue(userData)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    callback(true, "User added successfully")
                } else {
                    callback(false, task.exception?.message ?: "Failed to add user")
                }
            }
    }

    override fun getUserFromDatabase(userId: String, callback: (UserData?, Boolean, String) -> Unit) {
        ref.child(userId).get()
            .addOnSuccessListener { snapshot ->
                val user = snapshot.getValue(UserData::class.java)
                if (user != null) {
                    callback(user, true, "User found")
                } else {
                    callback(null, false, "User not found")
                }
            }
            .addOnFailureListener { exception ->
                callback(null, false, exception.message ?: "Failed to fetch user")
            }
    }

    override fun getCurrentUser(): FirebaseUser? {
        return auth.currentUser
    }

    override fun logout(callback: (Boolean, String) -> Unit) {
        auth.signOut()
        callback(true, "Logged out successfully")
    }

    override fun editProfile(userId: String, data: MutableMap<String, Any>, callback: (Boolean, String) -> Unit) {
        ref.child(userId).updateChildren(data)
            .addOnSuccessListener { callback(true, "Profile updated successfully") }
            .addOnFailureListener { callback(false, it.message ?: "Profile update failed") }
    }
}
