package com.example.cashiq.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cashiq.model.IncomeModel
import com.example.cashiq.repository.IncomeRepository
import com.example.cashiq.repository.IncomeRepositoryImpl

class IncomeViewModel : ViewModel() {

    private val incomeRepository: IncomeRepository = IncomeRepositoryImpl()

    private val _incomes = MutableLiveData<List<IncomeModel>?>()
    val incomes: LiveData<List<IncomeModel>?> get() = _incomes

    private val _operationStatus = MutableLiveData<Pair<Boolean, String>>()
    val operationStatus: LiveData<Pair<Boolean, String>> get() = _operationStatus

    fun addIncome(income: IncomeModel) {
        incomeRepository.addIncome(income) { success, message ->
            _operationStatus.postValue(Pair(success, message))
        }
    }

    fun getIncomeById(incomeId: String) {
        incomeRepository.getIncomeById(incomeId) { income, success, message ->
            if (success) _incomes.postValue(listOf(income!!))
            else _operationStatus.postValue(Pair(false, message))
        }
    }

    fun getAllIncomes(userId: String) {
        incomeRepository.getAllIncomes(userId) { incomeList, success, message ->
            if (success) _incomes.postValue(incomeList)
            else _operationStatus.postValue(Pair(false, message))
        }
    }

    fun updateIncome(incomeId: String, data: Map<String, Any>) {
        incomeRepository.updateIncome(incomeId, data) { success, message ->
            _operationStatus.postValue(Pair(success, message))
        }
    }

    fun deleteIncome(incomeId: String) {
        incomeRepository.deleteIncome(incomeId) { success, message ->
            _operationStatus.postValue(Pair(success, message))
        }
    }
}
