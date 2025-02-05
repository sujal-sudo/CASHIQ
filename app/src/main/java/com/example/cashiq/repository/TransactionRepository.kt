package com.example.cashiq.repository

import com.example.cashiq.model.TransactionModel

interface TransactionRepository {

    fun addTransaction(transaction: TransactionModel, callback: (Boolean, String) -> Unit)
    fun updateTransaction(transactionId: String, data: MutableMap<String, Any>, callback: (Boolean, String) -> Unit)
    fun deleteTransaction(transactionId: String, callback: (Boolean, String) -> Unit)
    fun getTransactionById(transactionId: String, callback: (TransactionModel?, Boolean, String) -> Unit)
    fun getAllTransactions(callback: (List<TransactionModel>?, Boolean, String) -> Unit)
}
