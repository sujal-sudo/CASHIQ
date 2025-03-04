package com.example.cashiq.repository

import com.example.cashiq.model.IncomeModel
import com.google.firebase.database.*

class IncomeRepositoryImpl : IncomeRepository {

    private val database: DatabaseReference = FirebaseDatabase.getInstance().reference.child("incomes")

    override fun addIncome(income: IncomeModel, callback: (Boolean, String) -> Unit) {
        val id = database.push().key.toString()
        val incomeWithId = income.copy(id = id)
        database.child(id).setValue(incomeWithId).addOnCompleteListener {
            if (it.isSuccessful) callback(true, "Income added successfully.")
            else callback(false, it.exception?.message ?: "Error adding income.")
        }
    }

    override fun getIncomeById(incomeId: String, callback: (IncomeModel?, Boolean, String) -> Unit) {
        database.child(incomeId).get().addOnSuccessListener {
            if (it.exists()) {
                val income = it.getValue(IncomeModel::class.java)
                callback(income, true, "Income fetched successfully.")
            } else {
                callback(null, false, "Income not found.")
            }
        }.addOnFailureListener {
            callback(null, false, it.message ?: "Error fetching income.")
        }
    }

    override fun getAllIncomes(userId: String, callback: (List<IncomeModel>?, Boolean, String) -> Unit) {
        database.orderByChild("userId").equalTo(userId).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val incomes = mutableListOf<IncomeModel>()
                for (data in snapshot.children) {
                    val income = data.getValue(IncomeModel::class.java)
                    income?.let { incomes.add(it) }
                }
                callback(incomes, true, "Incomes fetched successfully.")
            }

            override fun onCancelled(error: DatabaseError) {
                callback(null, false, error.message)
            }
        })
    }

    override fun updateIncome(incomeId: String, data: Map<String, Any>, callback: (Boolean, String) -> Unit) {
        database.child(incomeId).updateChildren(data).addOnCompleteListener {
            if (it.isSuccessful) callback(true, "Income updated successfully.")
            else callback(false, it.exception?.message ?: "Update failed.")
        }
    }

    override fun deleteIncome(incomeId: String, callback: (Boolean, String) -> Unit) {
        database.child(incomeId).removeValue().addOnCompleteListener {
            if (it.isSuccessful) callback(true, "Income deleted successfully.")
            else callback(false, it.exception?.message ?: "Error deleting income.")
        }
    }
}
