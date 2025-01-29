package com.example.cashiq.repository

import com.example.cashiq.model.UserModel

interface UserRepository {

    fun addUser(user: UserModel, callback: (Boolean, String) -> Unit)
    fun updateUser(userId: String, data: MutableMap<String, Any>, callback: (Boolean, String) -> Unit)
    fun deleteUser(userId: String, callback: (Boolean, String) -> Unit)
    fun getUserById(userId: String, callback: (UserModel?, Boolean, String) -> Unit)
    fun getAllUsers(callback: (List<UserModel>?, Boolean, String) -> Unit)
}