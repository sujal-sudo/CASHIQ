package com.example.cashiq.repository

import com.example.cashiq.model.IncomeModel

interface IncomeRepository {
    fun addIncome(income: IncomeModel, callback: (Boolean, String) -> Unit)
    fun updateIncome(incomeId: String, data: MutableMap<String, Any>, callback: (Boolean, String) -> Unit)
    fun deleteIncome(incomeId: String, callback: (Boolean, String) -> Unit)
    fun getIncomeById(incomeId: String, callback: (IncomeModel?, Boolean, String) -> Unit)
    fun getAllIncomes(userId: String, callback: (List<IncomeModel>?, Boolean, String) -> Unit)
}
