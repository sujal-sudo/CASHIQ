package com.example.cashiq.repository

import com.example.cashiq.model.BudgetModel
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*

class BudgetRepositoryImpl : BudgetRepository {

    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val ref: DatabaseReference = database.reference.child("budgets")

    override fun addBudget(budget: BudgetModel, callback: (Boolean, String) -> Unit) {
        val id = ref.push().key.toString()
        val budgetWithId = budget.copy(id = id)

        ref.child(id).setValue(budgetWithId).addOnCompleteListener {
            if (it.isSuccessful) {
                callback(true, "Budget created successfully.")
            } else {
                callback(false, "${it.exception?.message}")
            }
        }
    }

    override fun getBudgets(userId: String, callback: (List<BudgetModel>?, Boolean, String) -> Unit) {
        ref.orderByChild("userId").equalTo(userId).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val budgets = mutableListOf<BudgetModel>()
                    for (eachData in snapshot.children) {
                        val budget = eachData.getValue(BudgetModel::class.java)
                        budget?.let { budgets.add(it) }
                    }
                    callback(budgets, true, "Budgets fetched successfully.")
                } else {
                    callback(null, false, "No budgets found.")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                callback(null, false, error.message)
            }
        })
    }
}
