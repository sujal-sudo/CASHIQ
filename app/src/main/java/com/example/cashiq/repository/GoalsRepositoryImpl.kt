package com.example.cashiq.repository

import com.example.cashiq.model.GoalsModel
import com.google.firebase.database.*

class GoalsRepositoryImpl : GoalsRepository {

    private val database: DatabaseReference = FirebaseDatabase.getInstance().reference.child("goals")

    override fun addGoal(goal: GoalsModel, callback: (Boolean, String) -> Unit) {
        val id = database.push().key.toString()
        val goalWithId = goal.copy(id = id)
        database.child(id).setValue(goalWithId).addOnCompleteListener {
            if (it.isSuccessful) callback(true, "Goal added successfully.")
            else callback(false, it.exception?.message ?: "Error adding goal.")
        }
    }

    override fun getGoalById(goalId: String, callback: (GoalsModel?, Boolean, String) -> Unit) {
        database.child(goalId).get().addOnSuccessListener {
            if (it.exists()) {
                val goal = it.getValue(GoalsModel::class.java)
                callback(goal, true, "Goal fetched successfully.")
            } else {
                callback(null, false, "Goal not found.")
            }
        }.addOnFailureListener {
            callback(null, false, it.message ?: "Error fetching goal.")
        }
    }

    override fun getAllGoals(userId: String, callback: (List<GoalsModel>?, Boolean, String) -> Unit) {
        database.orderByChild("userId").equalTo(userId).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val goals = mutableListOf<GoalsModel>()
                for (data in snapshot.children) {
                    val goal = data.getValue(GoalsModel::class.java)
                    goal?.let { goals.add(it) }
                }
                callback(goals, true, "Goals fetched successfully.")
            }

            override fun onCancelled(error: DatabaseError) {
                callback(null, false, error.message)
            }
        })
    }

    override fun updateGoal(goalId: String, data: Map<String, Any>, callback: (Boolean, String) -> Unit) {
        database.child(goalId).updateChildren(data).addOnCompleteListener {
            if (it.isSuccessful) callback(true, "Goal updated successfully.")
            else callback(false, it.exception?.message ?: "Update failed.")
        }
    }

    override fun deleteGoal(goalId: String, callback: (Boolean, String) -> Unit) {
        database.child(goalId).removeValue().addOnCompleteListener {
            if (it.isSuccessful) callback(true, "Goal deleted successfully.")
            else callback(false, it.exception?.message ?: "Error deleting goal.")
        }
    }
}
