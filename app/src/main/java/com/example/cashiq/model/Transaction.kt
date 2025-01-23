package com.example.cashiq.model
import com.example.cashiq.model.UserData
import com.google.firebase.database.DatabaseReference

private lateinit var databaseReference: DatabaseReference

data class Transaction(
    val transactionId : String = "",
    val userId: String? = null,
    val amount: Double,
    val category: String,
    val date: String,
    val isIncome: Boolean,
    val description: String = "" // Optional: Add a description if needed
)
