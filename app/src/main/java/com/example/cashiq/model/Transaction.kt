package com.example.cashiq.model

data class Transaction(
    val transactionId: String = "",  // Unique ID for each transaction
    val userId: String? = null,       // User ID linked to the transaction
    val amount: Double = 0.0,         // Amount of transaction
    val category: String = "",        // Category (e.g., Salary, Food)
    val date: String = "",            // Date of transaction (format: yyyy-MM-dd)
    val isIncome: Boolean = false,    // True = Income, False = Expense
    val description: String = ""      // Optional description
)
