package com.example.cashiq.model

data class IncomeModel(
    val id: String = "",           // Primary Key
    val amount: Int = 0,
    val category: String  = "",     // Income amount
    val incomeDate: String = "",   // Date as String (format: yyyy-MM-dd)
    val incomeNote: String = "",
    val userId: String = ""        // Foreign Key (User ID)
)
