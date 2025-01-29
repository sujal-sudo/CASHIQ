package com.example.cashiq.viewmodel

import androidx.lifecycle.MutableLiveData
import com.example.cashiq.model.UserModel
import com.example.cashiq.repository.UserRepository

class UserViewModel(val repo: UserRepository) {

    // Add User
    fun addUser(user: UserModel, callback: (Boolean, String) -> Unit) {
        repo.addUser(user, callback)
    }

    // Update User
    fun updateUser(userId: String, data: MutableMap<String, Any>, callback: (Boolean, String) -> Unit) {
        repo.updateUser(userId, data, callback)
    }

    // Delete User
    fun deleteUser(userId: String, callback: (Boolean, String) -> Unit) {
        repo.deleteUser(userId, callback)
    }

    // Get User by ID (LiveData for single user)
    private var _user = MutableLiveData<UserModel?>()
    var user = MutableLiveData<UserModel?>()
        get() = _user

    // Get All Users (LiveData for list of users)
    private var _allUsers = MutableLiveData<List<UserModel>>()
    var allUsers = MutableLiveData<List<UserModel>>()
        get() = _allUsers

    // Loading LiveData (for showing loading state)
    private var _loading = MutableLiveData<Boolean>()
    var loading = MutableLiveData<Boolean>()
        get() = _loading

    // Fetch single user by ID
    fun getUserById(userId: String) {
        repo.getUserById(userId) { userModel, success, message ->
            if (success) {
                _user.value = userModel
            } else {
                // Handle failure message if needed
            }
        }
    }

    // Fetch all users
    fun getAllUsers() {
        _loading.value = true
        repo.getAllUsers { users, success, message ->
            if (success) {
                // Check if users is null and assign an empty list if so
                _allUsers.value = users ?: emptyList()  // Default to empty list if null
                _loading.value = false
            } else {
                // Handle failure message if needed
                _allUsers.value = emptyList()
                _loading.value = false
            }
        }
    }
}
