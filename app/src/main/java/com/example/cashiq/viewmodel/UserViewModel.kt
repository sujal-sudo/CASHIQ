package com.example.cashiq.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cashiq.model.UserModel
import com.example.cashiq.repository.UserRepository
import com.example.cashiq.repository.UserRepositoryImpl

class UserViewModel : ViewModel() {

    private val userRepository: UserRepository = UserRepositoryImpl()

    private val _user = MutableLiveData<UserModel?>()
    val user: LiveData<UserModel?> get() = _user

    private val _authStatus = MutableLiveData<Pair<Boolean, String>>()
    val authStatus: LiveData<Pair<Boolean, String>> get() = _authStatus

    fun registerUser(user: UserModel) {
        userRepository.registerUser(user) { success, message ->
            _authStatus.postValue(Pair(success, message))
        }
    }

    fun loginUser(email: String, password: String) {
        userRepository.loginUser(email, password) { success, message ->
            _authStatus.postValue(Pair(success, message))
        }
    }

    fun updateUser(userId: String, data: Map<String, Any>) {
        userRepository.updateUser(userId, data) { success, message ->
            _authStatus.postValue(Pair(success, message))
        }
    }

    fun deleteUser(userId: String) {
        userRepository.deleteUser(userId) { success, message ->
            _authStatus.postValue(Pair(success, message))
        }
    }

    fun getUserById(userId: String) {
        userRepository.getUserById(userId) { user, success, message ->
            if (success) _user.postValue(user)
        }
    }
}
