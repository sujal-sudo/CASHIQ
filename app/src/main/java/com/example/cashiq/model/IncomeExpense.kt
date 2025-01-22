package com.example.cashiq.model

data class IncomeExpense(
    val amount: Double = 0.0,
    val description: String = "",
    val category: String = "",
    val isRecurring: Boolean = false
)
