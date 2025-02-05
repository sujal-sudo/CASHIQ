package com.example.cashiq.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cashiq.model.TransactionModel
import com.example.cashiq.repository.TransactionRepository

class TransactionViewModel(private val repo: TransactionRepository) : ViewModel() {

    // LiveData for a single transaction
    private var _transaction = MutableLiveData<TransactionModel?>()
    val transaction: MutableLiveData<TransactionModel?> get() = _transaction

    // LiveData for all transactions
    private var _allTransactions = MutableLiveData<List<TransactionModel>>()
    val allTransactions: MutableLiveData<List<TransactionModel>> get() = _allTransactions

    // LiveData for loading state
    private var _loading = MutableLiveData<Boolean>()
    val loading: MutableLiveData<Boolean> get() = _loading

    // Add a new transaction
    fun addTransaction(transactionModel: TransactionModel, callback: (Boolean, String) -> Unit) {
        repo.addTransaction(transactionModel, callback)
    }

    // Update a transaction
    fun updateTransaction(transactionId: String, data: MutableMap<String, Any>, callback: (Boolean, String) -> Unit) {
        repo.updateTransaction(transactionId, data, callback)
    }

    // Delete a transaction
    fun deleteTransaction(transactionId: String, callback: (Boolean, String) -> Unit) {
        repo.deleteTransaction(transactionId, callback)
    }

    // Fetch a single transaction
    fun getTransactionById(transactionId: String) {
        repo.getTransactionById(transactionId) { transactionModel, success, message ->
            if (success) {
                _transaction.value = transactionModel
            }
        }
    }

    // Fetch all transactions
    fun getAllTransactions() {
        _loading.value = true
        repo.getAllTransactions { transactions, success, message ->
            if (success) {
                _allTransactions.value = transactions ?: emptyList()
            } else {
                _allTransactions.value = emptyList()
            }
            _loading.value = false
        }
    }
}
