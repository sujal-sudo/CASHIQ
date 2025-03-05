package com.example.cashiq.model

data class TransactionModel(
    val id: String = "",
    val userId: String = "",
    val category: String = "",
    val amount: Int = 0,
    val date: String = "",
    val note: String = "",
    val type: String = "" // Can be "income" or "expense"
)
