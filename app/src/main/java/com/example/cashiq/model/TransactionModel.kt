package com.example.cashiq.model

data class TransactionModel(
    val transactionId: String = "",
    val transactionType: String = "",
    val transactionAmount: Double = 0.0,
    val transactionDate: String = "",
    val transactionNote: String = "" ,
)
