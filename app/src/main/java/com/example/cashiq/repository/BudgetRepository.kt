package com.example.cashiq.repository

import com.example.cashiq.model.BudgetModel

interface BudgetRepository {

    fun addBudget(budget: BudgetModel, callback: (Boolean, String) -> Unit)

    fun updateBudget(budgetId: String, data: MutableMap<String, Any>, callback: (Boolean, String) -> Unit)

    fun deleteBudget(budgetId: String, callback: (Boolean, String) -> Unit)

    fun getBudgetById(budgetId: String, callback: (BudgetModel?, Boolean, String) -> Unit)

    fun getAllBudgets(callback: (List<BudgetModel>?, Boolean, String) -> Unit)
}
