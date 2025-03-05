package com.example.cashiq.repository

import com.example.cashiq.model.ExpenseModel
import com.google.firebase.database.*

class ExpenseRepositoryImpl : ExpenseRepository {

    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val ref: DatabaseReference = database.reference.child("expenses")

    override fun addExpense(expense: ExpenseModel, callback: (Boolean, String) -> Unit) {
        val id = ref.push().key.toString()
        val expenseWithId = expense.copy(id = id)
        ref.child(id).setValue(expenseWithId).addOnCompleteListener {
            if (it.isSuccessful) callback(true, "Expense added successfully.")
            else callback(false, it.exception?.message ?: "Error adding expense.")
        }
    }

    override fun updateExpense(expenseId: String, data: MutableMap<String, Any>, callback: (Boolean, String) -> Unit) {
        ref.child(expenseId).updateChildren(data).addOnCompleteListener {
            if (it.isSuccessful) callback(true, "Expense updated successfully.")
            else callback(false, it.exception?.message ?: "Error updating expense.")
        }
    }

    override fun deleteExpense(expenseId: String, callback: (Boolean, String) -> Unit) {
        ref.child(expenseId).removeValue().addOnCompleteListener {
            if (it.isSuccessful) callback(true, "Expense deleted successfully.")
            else callback(false, it.exception?.message ?: "Error deleting expense.")
        }
    }

    override fun getExpenseById(expenseId: String, callback: (ExpenseModel?, Boolean, String) -> Unit) {
        ref.child(expenseId).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val expense = snapshot.getValue(ExpenseModel::class.java)
                callback(expense, expense != null, "Expense fetched successfully.")
            }

            override fun onCancelled(error: DatabaseError) {
                callback(null, false, error.message)
            }
        })
    }

    override fun getAllExpenses(userId: String, callback: (List<ExpenseModel>?, Boolean, String) -> Unit) {
        ref.orderByChild("userId").equalTo(userId).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val expenses = snapshot.children.mapNotNull { it.getValue(ExpenseModel::class.java) }
                callback(expenses, true, "Expenses fetched successfully.")
            }

            override fun onCancelled(error: DatabaseError) {
                callback(null, false, error.message)
            }
        })
    }
}
