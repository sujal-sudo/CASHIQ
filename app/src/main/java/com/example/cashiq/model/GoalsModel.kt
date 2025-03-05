package com.example.cashiq.model

data class GoalsModel(
    val id: String = "",            // Primary Key
    val name: String = "",          // Goal name
    val targetAmount: Int = 0,      // Total savings target
    val savedAmount: Int = 0,       // Current saved amount
    val startDate: String = "",     // Start date (yyyy-MM-dd)
    val endDate: String = "",       // End date (yyyy-MM-dd)
    val userId: String = ""         // Foreign Key (User ID)
)
