package com.example.cashiq.repository

import com.example.cashiq.model.TransactionModel
import com.google.firebase.database.*

class TransactionRepositoryImpl : TransactionRepository {

    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val ref: DatabaseReference = database.reference.child("transactions")

    // Add a new transaction
    override fun addTransaction(transaction: TransactionModel, callback: (Boolean, String) -> Unit) {
        val id = ref.push().key.toString() // Generate unique ID
        val newTransaction = transaction.copy(transactionId = id)

        ref.child(id).setValue(newTransaction).addOnCompleteListener {
            if (it.isSuccessful) {
                callback(true, "Transaction added successfully")
            } else {
                callback(false, it.exception?.message ?: "Error adding transaction")
            }
        }
    }

    // Update an existing transaction
    override fun updateTransaction(transactionId: String, data: MutableMap<String, Any>, callback: (Boolean, String) -> Unit) {
        ref.child(transactionId).updateChildren(data).addOnCompleteListener {
            if (it.isSuccessful) {
                callback(true, "Transaction updated successfully")
            } else {
                callback(false, it.exception?.message ?: "Error updating transaction")
            }
        }
    }

    // Delete a transaction
    override fun deleteTransaction(transactionId: String, callback: (Boolean, String) -> Unit) {
        ref.child(transactionId).removeValue().addOnCompleteListener {
            if (it.isSuccessful) {
                callback(true, "Transaction deleted successfully")
            } else {
                callback(false, it.exception?.message ?: "Error deleting transaction")
            }
        }
    }

    // Get a transaction by ID
    override fun getTransactionById(transactionId: String, callback: (TransactionModel?, Boolean, String) -> Unit) {
        ref.child(transactionId).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val transaction = snapshot.getValue(TransactionModel::class.java)
                    callback(transaction, true, "Transaction fetched successfully")
                } else {
                    callback(null, false, "Transaction not found")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                callback(null, false, error.message)
            }
        })
    }

    // Get all transactions
    override fun getAllTransactions(callback: (List<TransactionModel>?, Boolean, String) -> Unit) {
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val transactions = mutableListOf<TransactionModel>()
                    for (eachData in snapshot.children) {
                        val transaction = eachData.getValue(TransactionModel::class.java)
                        if (transaction != null) {
                            transactions.add(transaction)
                        }
                    }
                    callback(transactions, true, "Transactions fetched successfully")
                } else {
                    callback(emptyList(), false, "No transactions found")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                callback(null, false, error.message)
            }
        })
    }
}
