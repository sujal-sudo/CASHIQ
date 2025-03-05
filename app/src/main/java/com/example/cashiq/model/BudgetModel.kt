package com.example.cashiq.model

data class BudgetModel(
    val id: String = "",         // Primary Key (Firebase Auto-Generated ID)
    val userId: String = "",     // Foreign Key (User ID)
    val category: String = "",   // Budget Category
    val amount: Int = 0,         // Budget Amount from Slider
    val startDate: String = "",  // Start Date (yyyy-MM-dd)
    val endDate: String = ""     // End Date (Auto-calculated: One Month Later)
)
