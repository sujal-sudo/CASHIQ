package com.example.cashiq.model

data class Transaction(
    val amount: Double,
    val category: String,
    val date: String,
    val isIncome: Boolean,
    val description: String = "" // Optional: Add a description if needed
)
