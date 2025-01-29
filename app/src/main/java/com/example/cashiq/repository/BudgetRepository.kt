package com.example.cashiq.repository

import com.example.cashiq.model.BudgetModel1

interface BudgetRepository {

    fun addBudget(budget: BudgetModel1, callback: (Boolean, String) -> Unit)

    fun updateBudget(budgetId: String, data: MutableMap<String, Any>, callback: (Boolean, String) -> Unit)

    fun deleteBudget(budgetId: String, callback: (Boolean, String) -> Unit)

    fun getBudgetById(budgetId: String, callback: (BudgetModel1?, Boolean, String) -> Unit)

    fun getAllBudgets(callback: (List<BudgetModel1>?, Boolean, String) -> Unit)
}
