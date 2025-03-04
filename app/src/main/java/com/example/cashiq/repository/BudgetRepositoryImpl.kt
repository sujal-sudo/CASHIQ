package com.example.cashiq.repository

import com.example.cashiq.model.BudgetModel
import com.google.firebase.database.*

class BudgetRepositoryImpl : BudgetRepository {

    private val database: DatabaseReference = FirebaseDatabase.getInstance().reference.child("budgets")

    override fun addBudget(budget: BudgetModel, callback: (Boolean, String) -> Unit) {
        val id = database.push().key.toString()
        val budgetWithId = budget.copy(id = id)
        database.child(id).setValue(budgetWithId).addOnCompleteListener {
            if (it.isSuccessful) callback(true, "Budget added successfully.")
            else callback(false, it.exception?.message ?: "Error adding budget.")
        }
    }

    override fun getBudgetById(budgetId: String, callback: (BudgetModel?, Boolean, String) -> Unit) {
        database.child(budgetId).get().addOnSuccessListener {
            if (it.exists()) {
                val budget = it.getValue(BudgetModel::class.java)
                callback(budget, true, "Budget fetched successfully.")
            } else {
                callback(null, false, "Budget not found.")
            }
        }.addOnFailureListener {
            callback(null, false, it.message ?: "Error fetching budget.")
        }
    }

    override fun getAllBudgets(userId: String, callback: (List<BudgetModel>?, Boolean, String) -> Unit) {
        database.orderByChild("userId").equalTo(userId).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val budgets = mutableListOf<BudgetModel>()
                for (data in snapshot.children) {
                    val budget = data.getValue(BudgetModel::class.java)
                    budget?.let { budgets.add(it) }
                }
                callback(budgets, true, "Budgets fetched successfully.")
            }

            override fun onCancelled(error: DatabaseError) {
                callback(null, false, error.message)
            }
        })
    }

    override fun updateBudget(budgetId: String, data: Map<String, Any>, callback: (Boolean, String) -> Unit) {
        database.child(budgetId).updateChildren(data).addOnCompleteListener {
            if (it.isSuccessful) callback(true, "Budget updated successfully.")
            else callback(false, it.exception?.message ?: "Update failed.")
        }
    }

    override fun deleteBudget(budgetId: String, callback: (Boolean, String) -> Unit) {
        database.child(budgetId).removeValue().addOnCompleteListener {
            if (it.isSuccessful) callback(true, "Budget deleted successfully.")
            else callback(false, it.exception?.message ?: "Error deleting budget.")
        }
    }
}
