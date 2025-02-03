package com.example.cashiq.repository

import com.google.firebase.auth.FirebaseUser
import com.example.cashiq.model.UserData

interface UserRepository {
    fun loginUser(email: String, password: String, callback: (Boolean, String) -> Unit)
    fun signupUser(email: String, password: String, callback: (Boolean, String, String) -> Unit)
    fun addUserToDatabase(id: String, userData: UserData, callback: (Boolean, String) -> Unit)
    fun getUserFromDatabase(userId: String, callback: (UserData?, Boolean, String) -> Unit)
    fun getCurrentUser(): FirebaseUser?
    fun logout(callback: (Boolean, String) -> Unit)
    fun editProfile(userId: String, data: MutableMap<String, Any>, callback: (Boolean, String) -> Unit)
}
