package com.example.cashiq.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cashiq.model.ExpenseModel
import com.example.cashiq.repository.ExpenseRepository
import com.example.cashiq.repository.ExpenseRepositoryImpl

class ExpenseViewModel : ViewModel() {

    private val expenseRepo: ExpenseRepository = ExpenseRepositoryImpl()

    private val _expenses = MutableLiveData<List<ExpenseModel>>()
    val expenses: LiveData<List<ExpenseModel>> = _expenses

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    private val _operationStatus = MutableLiveData<Pair<Boolean, String>>() // Fix added
    val operationStatus: LiveData<Pair<Boolean, String>> = _operationStatus

    fun fetchExpenses(userId: String) {
        _loading.postValue(true)
        expenseRepo.getAllExpenses(userId) { list, success, message ->
            _loading.postValue(false)
            if (success) {
                _expenses.postValue(list)
            } else {
                _errorMessage.postValue(message)
            }
        }
    }

    fun addExpense(expense: ExpenseModel) {
        _loading.postValue(true)
        expenseRepo.addExpense(expense) { success, message ->
            _loading.postValue(false)
            _operationStatus.postValue(Pair(success, message)) // Fix added
            if (success) fetchExpenses(expense.userId)
        }
    }

    fun updateExpense(expenseId: String, data: MutableMap<String, Any>) {
        _loading.postValue(true)
        expenseRepo.updateExpense(expenseId, data) { success, message ->
            _loading.postValue(false)
            _operationStatus.postValue(Pair(success, message)) // Fix added
        }
    }

    fun deleteExpense(expenseId: String, userId: String) {
        _loading.postValue(true)
        expenseRepo.deleteExpense(expenseId) { success, message ->
            _loading.postValue(false)
            _operationStatus.postValue(Pair(success, message)) // Fix added
            if (success) fetchExpenses(userId)
        }
    }
}
