package com.example.cashiq.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cashiq.model.TransactionModel
import com.example.cashiq.repository.ExpenseRepositoryImpl
import com.example.cashiq.repository.IncomeRepositoryImpl

class TransactionViewModel : ViewModel() {

    private val incomeRepository = IncomeRepositoryImpl()
    private val expenseRepository = ExpenseRepositoryImpl()

    private val _transactions = MutableLiveData<List<TransactionModel>>()
    val transactions: LiveData<List<TransactionModel>> get() = _transactions

    fun getAllTransactions(userId: String) {
        Log.d("TransactionViewModel", "Fetching transactions for user: $userId")

        val allTransactions = mutableListOf<TransactionModel>()

        incomeRepository.getAllIncomes(userId) { incomeList, success, message ->
            if (success && incomeList != null) {
                val incomeTransactions = incomeList.map { it.toTransactionModel("income") }
                allTransactions.addAll(incomeTransactions)
                Log.d("TransactionViewModel", "Fetched ${incomeTransactions.size} incomes")
            } else {
                Log.e("TransactionViewModel", "Failed to fetch incomes: $message")
            }

            expenseRepository.getAllExpenses(userId) { expenseList, success, message ->
                if (success && expenseList != null) {
                    val expenseTransactions = expenseList.map { it.toTransactionModel("expense") }
                    allTransactions.addAll(expenseTransactions)
                    Log.d("TransactionViewModel", "Fetched ${expenseTransactions.size} expenses")
                } else {
                    Log.e("TransactionViewModel", "Failed to fetch expenses: $message")
                }

                // ✅ Sort transactions by date descending
                allTransactions.sortByDescending { it.date }
                _transactions.postValue(allTransactions)
                Log.d("TransactionViewModel", "Total Transactions Loaded: ${allTransactions.size}")
            }
        }
    }
}

// ✅ Convert IncomeModel & ExpenseModel to TransactionModel
private fun com.example.cashiq.model.IncomeModel.toTransactionModel(type: String) =
    TransactionModel(id, userId, category, amount, incomeDate, incomeNote, type)

private fun com.example.cashiq.model.ExpenseModel.toTransactionModel(type: String) =
    TransactionModel(id, userId, category, amount, expenseDate, expenseNote, type)
