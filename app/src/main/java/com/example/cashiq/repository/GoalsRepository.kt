package com.example.cashiq.repository

import com.example.cashiq.model.GoalsModel

interface GoalsRepository {
    fun addGoal(goal: GoalsModel, callback: (Boolean, String) -> Unit)
    fun getGoalById(goalId: String, callback: (GoalsModel?, Boolean, String) -> Unit)
    fun getAllGoals(userId: String, callback: (List<GoalsModel>?, Boolean, String) -> Unit)
    fun updateGoal(goalId: String, data: Map<String, Any>, callback: (Boolean, String) -> Unit)
    fun deleteGoal(goalId: String, callback: (Boolean, String) -> Unit)
}
