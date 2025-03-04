package com.example.cashiq.model

data class BudgetModel(
    val id: String = "",             // Primary Key
    val budgetAmount: Int = 0,        // Total budget amount
    val startDate: String = "",       // Start date (yyyy-MM-dd)
    val endDate: String = "",         // End date (yyyy-MM-dd)
    val budgetCategory: String = "",  // Category of budget
    val userId: String = ""           // Foreign Key (User ID)
)
