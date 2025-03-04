package com.example.cashiq.repository

import com.example.cashiq.model.ExpenseModel
import com.google.firebase.database.*

class ExpenseRepositoryImpl : ExpenseRepository {

    private val database: DatabaseReference = FirebaseDatabase.getInstance().reference.child("expenses")

    override fun addExpense(expense: ExpenseModel, callback: (Boolean, String) -> Unit) {
        val id = database.push().key.toString()
        val expenseWithId = expense.copy(id = id)
        database.child(id).setValue(expenseWithId).addOnCompleteListener {
            if (it.isSuccessful) callback(true, "Expense added successfully.")
            else callback(false, it.exception?.message ?: "Error adding expense.")
        }
    }

    override fun getExpenseById(expenseId: String, callback: (ExpenseModel?, Boolean, String) -> Unit) {
        database.child(expenseId).get().addOnSuccessListener {
            if (it.exists()) {
                val expense = it.getValue(ExpenseModel::class.java)
                callback(expense, true, "Expense fetched successfully.")
            } else {
                callback(null, false, "Expense not found.")
            }
        }.addOnFailureListener {
            callback(null, false, it.message ?: "Error fetching expense.")
        }
    }

    override fun getAllExpenses(userId: String, callback: (List<ExpenseModel>?, Boolean, String) -> Unit) {
        database.orderByChild("userId").equalTo(userId).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val expenses = mutableListOf<ExpenseModel>()
                for (data in snapshot.children) {
                    val expense = data.getValue(ExpenseModel::class.java)
                    expense?.let { expenses.add(it) }
                }
                callback(expenses, true, "Expenses fetched successfully.")
            }

            override fun onCancelled(error: DatabaseError) {
                callback(null, false, error.message)
            }
        })
    }

    override fun updateExpense(expenseId: String, data: Map<String, Any>, callback: (Boolean, String) -> Unit) {
        database.child(expenseId).updateChildren(data).addOnCompleteListener {
            if (it.isSuccessful) callback(true, "Expense updated successfully.")
            else callback(false, it.exception?.message ?: "Update failed.")
        }
    }

    override fun deleteExpense(expenseId: String, callback: (Boolean, String) -> Unit) {
        database.child(expenseId).removeValue().addOnCompleteListener {
            if (it.isSuccessful) callback(true, "Expense deleted successfully.")
            else callback(false, it.exception?.message ?: "Error deleting expense.")
        }
    }
}
