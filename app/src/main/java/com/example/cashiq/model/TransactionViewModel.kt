package com.example.cashiq.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cashiq.model.Transaction

class TransactionViewModel : ViewModel() {
    private val _transactions = MutableLiveData<List<Transaction>>()
    val transactions: LiveData<List<Transaction>> = _transactions

    fun addTransaction(transaction: Transaction) {
        val currentList = _transactions.value.orEmpty().toMutableList()
        currentList.add(transaction)
        _transactions.value = currentList
    }

    fun setTransactions(transactions: List<Transaction>) {
        _transactions.value = transactions
    }
}