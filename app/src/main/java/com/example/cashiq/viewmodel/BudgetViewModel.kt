package com.example.cashiq.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cashiq.model.BudgetModel
import com.example.cashiq.repository.BudgetRepository
import com.example.cashiq.repository.BudgetRepositoryImpl
import java.text.SimpleDateFormat
import java.util.*

class BudgetViewModel : ViewModel() {

    private val budgetRepository: BudgetRepository = BudgetRepositoryImpl()

    private val _budgets = MutableLiveData<List<BudgetModel>>()
    val budgets: LiveData<List<BudgetModel>> get() = _budgets

    private val _operationStatus = MutableLiveData<Pair<Boolean, String>>()
    val operationStatus: LiveData<Pair<Boolean, String>> get() = _operationStatus

    fun addBudget(category: String, amount: Int, userId: String) {
        val startDate = getCurrentDate()
        val endDate = getOneMonthLater(startDate)

        val budget = BudgetModel(
            id = "",
            userId = userId,
            category = category,
            amount = amount,
            startDate = startDate,
            endDate = endDate
        )

        budgetRepository.addBudget(budget) { success, message ->
            _operationStatus.postValue(Pair(success, message))
            if (success) getBudgets(userId) // Refresh list after adding
        }
    }

    fun getBudgets(userId: String) {
        budgetRepository.getBudgets(userId) { budgets, success, message ->
            _budgets.postValue(budgets ?: emptyList())
            _operationStatus.postValue(Pair(success, message))
        }
    }

    fun deleteBudget(budgetId: String, userId: String) {
        budgetRepository.deleteBudget(budgetId) { success, message ->
            _operationStatus.postValue(Pair(success, message))
            if (success) getBudgets(userId) // Refresh list after deletion
        }
    }

    private fun getCurrentDate(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return sdf.format(Date())
    }

    private fun getOneMonthLater(startDate: String): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val date = sdf.parse(startDate)
        val calendar = Calendar.getInstance()
        calendar.time = date!!
        calendar.add(Calendar.MONTH, 1) // Add 1 month
        return sdf.format(calendar.time)
    }
}
