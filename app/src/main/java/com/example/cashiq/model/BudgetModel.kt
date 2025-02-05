package com.example.cashiq.model

data class BudgetModel(
    val budgetId: String = "",
    val budgetAmount: Double = 0.0,
    val startDate: String = "",
    val endDate: String = ""
)