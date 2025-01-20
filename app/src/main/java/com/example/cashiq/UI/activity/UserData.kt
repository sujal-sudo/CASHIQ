package com.example.cashiq.UI.activity

data class UserData(
    // storing 3 data in realtime database
    val id: String? = null,
    val username: String? = null,
    val email: String? =null,
    val password: String? = null,
    val transactionIds: List<String>? = null // List to hold transaction IDs
)
