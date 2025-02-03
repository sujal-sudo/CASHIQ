package com.example.cashiq.UI.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.cashiq.model.UserData
import com.example.cashiq.repository.UserRepository
import com.google.firebase.auth.FirebaseUser

class UserViewModel(private val repo: UserRepository) : ViewModel() {

    fun loginUser(email: String, password: String, callback: (Boolean, String) -> Unit) {
        repo.loginUser(email, password, callback)
    }

    fun signupUser(email: String, password: String, callback: (Boolean, String, String) -> Unit) {
        repo.signupUser(email, password, callback)
    }

    fun addUserToDatabase(userId: String, userModel: UserData, callback: (Boolean, String) -> Unit) {
        repo.addUserToDatabase(userId, userModel, callback)
    }

    fun getCurrentUser(): FirebaseUser? {
        return repo.getCurrentUser()
    }

    private val _userData = MutableLiveData<UserData?>()
    val userData: LiveData<UserData?> get() = _userData

    fun getUserFromDatabase(userId: String) {
        repo.getUserFromDatabase(userId) { userModel, success, message ->
            if (success) {
                _userData.value = userModel
            } else {
                _userData.value = null
            }
        }
    }

    fun logout(callback: (Boolean, String) -> Unit) {
        repo.logout(callback)
    }

    fun editProfile(userId: String, data: MutableMap<String, Any>, callback: (Boolean, String) -> Unit) {
        repo.editProfile(userId, data, callback)
    }
}
