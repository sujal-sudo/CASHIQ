package com.example.cashiq.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cashiq.model.IncomeModel
import com.example.cashiq.repository.IncomeRepository
import com.example.cashiq.repository.IncomeRepositoryImpl

class IncomeViewModel : ViewModel() {

    private val incomeRepo: IncomeRepository = IncomeRepositoryImpl()

    private val _incomes = MutableLiveData<List<IncomeModel>>()
    val incomes: LiveData<List<IncomeModel>> = _incomes

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    private val _operationStatus = MutableLiveData<Pair<Boolean, String>>()
    val operationStatus: LiveData<Pair<Boolean, String>> = _operationStatus

    fun fetchIncomes(userId: String) {
        _loading.postValue(true)
        incomeRepo.getAllIncomes(userId) { list, success, message ->
            _loading.postValue(false)
            if (success) {
                _incomes.postValue(list)
            } else {
                _errorMessage.postValue(message)
            }
        }
    }

    fun addIncome(income: IncomeModel) {
        _loading.postValue(true)
        incomeRepo.addIncome(income) { success, message ->
            _loading.postValue(false)
            _operationStatus.postValue(Pair(success, message)) // Update status
            if (success) fetchIncomes(income.userId)
        }
    }

    fun updateIncome(incomeId: String, data: MutableMap<String, Any>) {
        _loading.postValue(true)
        incomeRepo.updateIncome(incomeId, data) { success, message ->
            _loading.postValue(false)
            _operationStatus.postValue(Pair(success, message)) // Update status
        }
    }

    fun deleteIncome(incomeId: String, userId: String) {
        _loading.postValue(true)
        incomeRepo.deleteIncome(incomeId) { success, message ->
            _loading.postValue(false)
            _operationStatus.postValue(Pair(success, message)) // Update status
            if (success) fetchIncomes(userId)
        }
    }
}
