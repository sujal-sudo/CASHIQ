package com.example.cashiq.repository

import android.util.Log
import com.example.cashiq.model.BudgetModel
import com.google.firebase.database.*

class BudgetRepositoryImpl : BudgetRepository {

    private val database: DatabaseReference = FirebaseDatabase.getInstance().reference.child("budgets")

    // ✅ Add Budget to Firebase
    override fun addBudget(budget: BudgetModel, callback: (Boolean, String) -> Unit) {
        val id = database.push().key ?: return
        val budgetWithId = budget.copy(id = id)

        Log.d("BudgetRepository", "🔄 Adding Budget: $budgetWithId")

        database.child(id).setValue(budgetWithId)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("BudgetRepository", "✅ Budget Added Successfully")
                    callback(true, "Budget added successfully.")
                } else {
                    Log.e("BudgetRepository", "❌ Budget Add Failed: ${task.exception?.message}")
                    callback(false, task.exception?.message ?: "Unknown error")
                }
            }
            .addOnFailureListener { exception ->
                Log.e("BudgetRepository", "❌ Firebase Write Error: ${exception.message}")
                callback(false, exception.message ?: "Firebase error occurred.")
            }
    }

    // ✅ Retrieve Budgets for a Specific User (Live Updates)
    override fun getBudgets(userId: String, callback: (List<BudgetModel>?, Boolean, String) -> Unit) {
        database.orderByChild("userId").equalTo(userId)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val budgets = mutableListOf<BudgetModel>()
                    for (data in snapshot.children) {
                        val budget = data.getValue(BudgetModel::class.java)
                        budget?.let { budgets.add(it) }
                    }
                    Log.d("BudgetRepository", "✅ Retrieved ${budgets.size} Budgets")
                    callback(budgets, true, "Budgets retrieved successfully.")
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("BudgetRepository", "❌ Firebase Read Error: ${error.message}")
                    callback(null, false, error.message)
                }
            })
    }

    // ✅ Delete Budget by ID
    override fun deleteBudget(budgetId: String, callback: (Boolean, String) -> Unit) {
        Log.d("BudgetRepository", "🔄 Deleting Budget ID: $budgetId")

        database.child(budgetId).removeValue()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("BudgetRepository", "✅ Budget Deleted Successfully")
                    callback(true, "Budget deleted successfully.")
                } else {
                    Log.e("BudgetRepository", "❌ Budget Deletion Failed: ${task.exception?.message}")
                    callback(false, task.exception?.message ?: "Unknown error")
                }
            }
            .addOnFailureListener { exception ->
                Log.e("BudgetRepository", "❌ Firebase Delete Error: ${exception.message}")
                callback(false, exception.message ?: "Firebase error occurred.")
            }
    }
}
