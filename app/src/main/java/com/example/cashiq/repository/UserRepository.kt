package com.example.cashiq.repository

import com.example.cashiq.model.UserModel

interface UserRepository {
    fun registerUser(user: UserModel, callback: (Boolean, String) -> Unit)
    fun loginUser(email: String, password: String, callback: (Boolean, String) -> Unit)
    fun updateUser(userId: String, data: Map<String, Any>, callback: (Boolean, String) -> Unit)
    fun deleteUser(userId: String, callback: (Boolean, String) -> Unit)
    fun getUserById(userId: String, callback: (UserModel?, Boolean, String) -> Unit)
}
