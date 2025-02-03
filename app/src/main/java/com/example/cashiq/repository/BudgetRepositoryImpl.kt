package com.example.cashiq.repository

import com.example.cashiq.model.BudgetModel
import com.google.firebase.database.*

class BudgetRepositoryImpl : BudgetRepository {

    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val ref: DatabaseReference = database.reference.child("budgets")

    override fun addBudget(budget: BudgetModel, callback: (Boolean, String) -> Unit) {
        val id = ref.push().key.toString()
        val newBudget = budget.copy(budgetId = id)

        ref.child(id).setValue(newBudget).addOnCompleteListener {
            if (it.isSuccessful) {
                callback(true, "Budget added successfully")
            } else {
                callback(false, it.exception?.message ?: "Error adding budget")
            }
        }
    }

    override fun updateBudget(budgetId: String, data: MutableMap<String, Any>, callback: (Boolean, String) -> Unit) {
        ref.child(budgetId).updateChildren(data).addOnCompleteListener {
            if (it.isSuccessful) {
                callback(true, "Budget updated successfully")
            } else {
                callback(false, it.exception?.message ?: "Error updating budget")
            }
        }
    }

    override fun deleteBudget(budgetId: String, callback: (Boolean, String) -> Unit) {
        ref.child(budgetId).removeValue().addOnCompleteListener {
            if (it.isSuccessful) {
                callback(true, "Budget deleted successfully")
            } else {
                callback(false, it.exception?.message ?: "Error deleting budget")
            }
        }
    }

    override fun getBudgetById(budgetId: String, callback: (BudgetModel?, Boolean, String) -> Unit) {
        ref.child(budgetId).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val budget = snapshot.getValue(BudgetModel::class.java)
                    callback(budget, true, "Budget fetched successfully")
                } else {
                    callback(null, false, "Budget not found")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                callback(null, false, error.message)
            }
        })
    }

    override fun getAllBudgets(callback: (List<BudgetModel>?, Boolean, String) -> Unit) {
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val budgets = mutableListOf<BudgetModel>()
                    for (eachData in snapshot.children) {
                        val budget = eachData.getValue(BudgetModel::class.java)
                        if (budget != null) {
                            budgets.add(budget)
                        }
                    }
                    callback(budgets, true, "Budgets fetched successfully")
                } else {
                    callback(emptyList(), false, "No budgets found")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                callback(null, false, error.message)
            }
        })
    }
}
