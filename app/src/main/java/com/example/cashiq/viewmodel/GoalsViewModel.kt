package com.example.cashiq.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cashiq.model.GoalsModel
import com.example.cashiq.repository.GoalsRepository
import com.example.cashiq.repository.GoalsRepositoryImpl

class GoalsViewModel : ViewModel() {

    private val goalsRepository: GoalsRepository = GoalsRepositoryImpl()

    private val _goals = MutableLiveData<List<GoalsModel>?>()
    val goals: LiveData<List<GoalsModel>?> get() = _goals

    private val _operationStatus = MutableLiveData<Pair<Boolean, String>>()
    val operationStatus: LiveData<Pair<Boolean, String>> get() = _operationStatus

    fun addGoal(goal: GoalsModel) {
        goalsRepository.addGoal(goal) { success, message ->
            _operationStatus.postValue(Pair(success, message))
        }
    }

    fun getGoalById(goalId: String) {
        goalsRepository.getGoalById(goalId) { goal, success, message ->
            if (success) _goals.postValue(listOf(goal!!))
            else _operationStatus.postValue(Pair(false, message))
        }
    }

    fun getAllGoals(userId: String) {
        goalsRepository.getAllGoals(userId) { goalList, success, message ->
            if (success) _goals.postValue(goalList)
            else _operationStatus.postValue(Pair(false, message))
        }
    }

    fun updateGoal(goalId: String, data: Map<String, Any>) {
        goalsRepository.updateGoal(goalId, data) { success, message ->
            _operationStatus.postValue(Pair(success, message))
        }
    }

    fun deleteGoal(goalId: String) {
        goalsRepository.deleteGoal(goalId) { success, message ->
            _operationStatus.postValue(Pair(success, message))
        }
    }
}
