package com.example.cashiq.model

data class ExpenseModel(
    val id: String = "",           // Primary Key
    val amount: Int = 0,           // Expense amount
    val expenseDate: String = "",  // Date as String (format: yyyy-MM-dd)
    val expenseNote: String = "",
    val userId: String = ""        // Foreign Key (User ID)
)
