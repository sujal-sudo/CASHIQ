package com.example.cashiq.repository

import com.example.cashiq.model.ExpenseModel

interface ExpenseRepository {
    fun addExpense(expense: ExpenseModel, callback: (Boolean, String) -> Unit)
    fun updateExpense(expenseId: String, data: MutableMap<String, Any>, callback: (Boolean, String) -> Unit)
    fun deleteExpense(expenseId: String, callback: (Boolean, String) -> Unit)
    fun getExpenseById(expenseId: String, callback: (ExpenseModel?, Boolean, String) -> Unit)
    fun getAllExpenses(userId: String, callback: (List<ExpenseModel>?, Boolean, String) -> Unit)
}
