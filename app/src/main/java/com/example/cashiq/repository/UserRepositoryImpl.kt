package com.example.cashiq.repository

import com.example.cashiq.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class UserRepositoryImpl : UserRepository {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val database: DatabaseReference = FirebaseDatabase.getInstance().reference.child("users")

    override fun registerUser(user: UserModel, callback: (Boolean, String) -> Unit) {
        auth.createUserWithEmailAndPassword(user.email, user.password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val userId = auth.currentUser?.uid ?: ""
                val newUser = user.copy(id = userId)

                database.child(userId).setValue(newUser).addOnCompleteListener {
                    if (it.isSuccessful) callback(true, "User registered successfully.")
                    else callback(false, it.exception?.message ?: "Database error.")
                }
            } else {
                callback(false, task.exception?.message ?: "Registration failed.")
            }
        }
    }

    override fun loginUser(email: String, password: String, callback: (Boolean, String) -> Unit) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) callback(true, "Login successful.")
            else callback(false, task.exception?.message ?: "Login failed.")
        }
    }

    override fun updateUser(userId: String, data: Map<String, Any>, callback: (Boolean, String) -> Unit) {
        database.child(userId).updateChildren(data).addOnCompleteListener {
            if (it.isSuccessful) callback(true, "User updated successfully.")
            else callback(false, it.exception?.message ?: "Update failed.")
        }
    }

    override fun deleteUser(userId: String, callback: (Boolean, String) -> Unit) {
        database.child(userId).removeValue().addOnCompleteListener {
            if (it.isSuccessful) {
                auth.currentUser?.delete()?.addOnCompleteListener { task ->
                    if (task.isSuccessful) callback(true, "User deleted successfully.")
                    else callback(false, task.exception?.message ?: "Account deletion failed.")
                }
            } else {
                callback(false, it.exception?.message ?: "Database deletion failed.")
            }
        }
    }

    override fun getUserById(userId: String, callback: (UserModel?, Boolean, String) -> Unit) {
        database.child(userId).get().addOnSuccessListener {
            if (it.exists()) {
                val user = it.getValue(UserModel::class.java)
                callback(user, true, "User fetched successfully.")
            } else {
                callback(null, false, "User not found.")
            }
        }.addOnFailureListener {
            callback(null, false, it.message ?: "Error fetching user.")
        }
    }
}
