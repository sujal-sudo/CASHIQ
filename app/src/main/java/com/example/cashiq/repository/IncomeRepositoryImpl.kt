package com.example.cashiq.repository

import com.example.cashiq.model.IncomeModel
import com.google.firebase.database.*

class IncomeRepositoryImpl : IncomeRepository {

    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val ref: DatabaseReference = database.reference.child("incomes")

    override fun addIncome(income: IncomeModel, callback: (Boolean, String) -> Unit) {
        val id = ref.push().key.toString()
        val incomeWithId = income.copy(id = id)
        ref.child(id).setValue(incomeWithId).addOnCompleteListener {
            if (it.isSuccessful) callback(true, "Income added successfully.")
            else callback(false, it.exception?.message ?: "Error adding income.")
        }
    }

    override fun updateIncome(incomeId: String, data: MutableMap<String, Any>, callback: (Boolean, String) -> Unit) {
        ref.child(incomeId).updateChildren(data).addOnCompleteListener {
            if (it.isSuccessful) callback(true, "Income updated successfully.")
            else callback(false, it.exception?.message ?: "Error updating income.")
        }
    }

    override fun deleteIncome(incomeId: String, callback: (Boolean, String) -> Unit) {
        ref.child(incomeId).removeValue().addOnCompleteListener {
            if (it.isSuccessful) callback(true, "Income deleted successfully.")
            else callback(false, it.exception?.message ?: "Error deleting income.")
        }
    }

    override fun getIncomeById(incomeId: String, callback: (IncomeModel?, Boolean, String) -> Unit) {
        ref.child(incomeId).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val income = snapshot.getValue(IncomeModel::class.java)
                callback(income, income != null, "Income fetched successfully.")
            }

            override fun onCancelled(error: DatabaseError) {
                callback(null, false, error.message)
            }
        })
    }

    override fun getAllIncomes(userId: String, callback: (List<IncomeModel>?, Boolean, String) -> Unit) {
        ref.orderByChild("userId").equalTo(userId).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val incomes = snapshot.children.mapNotNull { it.getValue(IncomeModel::class.java) }
                callback(incomes, true, "Incomes fetched successfully.")
            }

            override fun onCancelled(error: DatabaseError) {
                callback(null, false, error.message)
            }
        })
    }
}
