package com.example.cashiq.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cashiq.model.BudgetModel
import com.example.cashiq.repository.BudgetRepository
import com.example.cashiq.repository.BudgetRepositoryImpl

class BudgetViewModel : ViewModel() {

    private val budgetRepository: BudgetRepository = BudgetRepositoryImpl()

    private val _budgets = MutableLiveData<List<BudgetModel>?>()
    val budgets: LiveData<List<BudgetModel>?> get() = _budgets

    private val _operationStatus = MutableLiveData<Pair<Boolean, String>>()
    val operationStatus: LiveData<Pair<Boolean, String>> get() = _operationStatus

    fun addBudget(budget: BudgetModel) {
        budgetRepository.addBudget(budget) { success, message ->
            _operationStatus.postValue(Pair(success, message))
        }
    }

    fun getBudgetById(budgetId: String) {
        budgetRepository.getBudgetById(budgetId) { budget, success, message ->
            if (success) _budgets.postValue(listOf(budget!!))
            else _operationStatus.postValue(Pair(false, message))
        }
    }

    fun getAllBudgets(userId: String) {
        budgetRepository.getAllBudgets(userId) { budgetList, success, message ->
            if (success) _budgets.postValue(budgetList)
            else _operationStatus.postValue(Pair(false, message))
        }
    }

    fun updateBudget(budgetId: String, data: Map<String, Any>) {
        budgetRepository.updateBudget(budgetId, data) { success, message ->
            _operationStatus.postValue(Pair(success, message))
        }
    }

    fun deleteBudget(budgetId: String) {
        budgetRepository.deleteBudget(budgetId) { success, message ->
            _operationStatus.postValue(Pair(success, message))
        }
    }
}
