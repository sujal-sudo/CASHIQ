package com.example.cashiq.viewmodel

import androidx.lifecycle.MutableLiveData
import com.example.cashiq.model.BudgetModel1
import com.example.cashiq.repository.BudgetRepository

class BudgetViewModel(private val repo: BudgetRepository) {

    // Add Budget
    fun addBudget(budget: BudgetModel1, callback: (Boolean, String) -> Unit) {
        repo.addBudget(budget, callback)
    }

    // Update Budget
    fun updateBudget(budgetId: String, data: MutableMap<String, Any>, callback: (Boolean, String) -> Unit) {
        repo.updateBudget(budgetId, data, callback)
    }

    // Delete Budget
    fun deleteBudget(budgetId: String, callback: (Boolean, String) -> Unit) {
        repo.deleteBudget(budgetId, callback)
    }

    // LiveData for a single budget
    private var _budget = MutableLiveData<BudgetModel1?>()
    var budget = MutableLiveData<BudgetModel1?>()
        get() = _budget

    // LiveData for all budgets
    private var _allBudgets = MutableLiveData<List<BudgetModel1>>()
    var allBudgets = MutableLiveData<List<BudgetModel1>>()
        get() = _allBudgets

    // LiveData for loading state
    private var _loading = MutableLiveData<Boolean>()
    var loading = MutableLiveData<Boolean>()
        get() = _loading

    // Fetch a single budget by ID
    fun getBudgetById(budgetId: String) {
        repo.getBudgetById(budgetId) { budgetModel, success, message ->
            if (success) {
                _budget.value = budgetModel
            }
        }
    }

    // Fetch all budgets
    fun getAllBudgets() {
        _loading.value = true
        repo.getAllBudgets { budgets, success, message ->
            if (success) {
                _allBudgets.value = budgets ?: emptyList() // Ensure non-null list
                _loading.value = false
            } else {
                _allBudgets.value = emptyList() // Handle failure by providing empty list
                _loading.value = false
            }
        }
    }
}
