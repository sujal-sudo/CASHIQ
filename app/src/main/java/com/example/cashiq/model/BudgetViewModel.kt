package com.example.cashiq.model

data class Budget(
    val budgetId: String = "",
    val userId: String = "",
    val category: String = "",
    val amount: Double = 0.0,
    val startDate: String = "", // Use ISO format
    val endDate: String = ""   // Use ISO format
)
