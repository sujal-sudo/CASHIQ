package com.example.cashiq.repository

import com.example.cashiq.model.BudgetModel

interface BudgetRepository {
    fun addBudget(budget: BudgetModel, callback: (Boolean, String) -> Unit)
    fun getBudgets(userId: String, callback: (List<BudgetModel>?, Boolean, String) -> Unit)
}
