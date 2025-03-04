package com.example.cashiq.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cashiq.model.ExpenseModel
import com.example.cashiq.repository.ExpenseRepository
import com.example.cashiq.repository.ExpenseRepositoryImpl

class ExpenseViewModel : ViewModel() {

    private val expenseRepository: ExpenseRepository = ExpenseRepositoryImpl()

    private val _expenses = MutableLiveData<List<ExpenseModel>?>()
    val expenses: LiveData<List<ExpenseModel>?> get() = _expenses

    private val _operationStatus = MutableLiveData<Pair<Boolean, String>>()
    val operationStatus: LiveData<Pair<Boolean, String>> get() = _operationStatus

    fun addExpense(expense: ExpenseModel) {
        expenseRepository.addExpense(expense) { success, message ->
            _operationStatus.postValue(Pair(success, message))
        }
    }

    fun getExpenseById(expenseId: String) {
        expenseRepository.getExpenseById(expenseId) { expense, success, message ->
            if (success) _expenses.postValue(listOf(expense!!))
            else _operationStatus.postValue(Pair(false, message))
        }
    }

    fun getAllExpenses(userId: String) {
        expenseRepository.getAllExpenses(userId) { expenseList, success, message ->
            if (success) _expenses.postValue(expenseList)
            else _operationStatus.postValue(Pair(false, message))
        }
    }

    fun updateExpense(expenseId: String, data: Map<String, Any>) {
        expenseRepository.updateExpense(expenseId, data) { success, message ->
            _operationStatus.postValue(Pair(success, message))
        }
    }

    fun deleteExpense(expenseId: String) {
        expenseRepository.deleteExpense(expenseId) { success, message ->
            _operationStatus.postValue(Pair(success, message))
        }
    }
}
