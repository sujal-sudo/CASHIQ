package com.example.cashiq.repository

interface AuthRepo {
    fun login(email: String, password: String, callback: (Boolean, String) -> Unit)
    fun register(email: String, password: String, callback: (Boolean, String) -> Unit)
    fun logout()
    fun getCurrentUserId(): String?
}