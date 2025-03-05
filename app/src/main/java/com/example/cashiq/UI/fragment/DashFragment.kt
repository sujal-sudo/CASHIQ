package com.example.cashiq.UI.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.cashiq.UI.activity.ExpenseActivity
import com.example.cashiq.UI.activity.IncomeActivity
import com.example.cashiq.databinding.FragmentDashBinding
import com.example.cashiq.viewmodel.BudgetViewModel
import com.example.cashiq.viewmodel.ExpenseViewModel
import com.example.cashiq.viewmodel.IncomeViewModel
import com.google.android.material.tabs.TabLayoutMediator
import java.text.SimpleDateFormat
import java.util.*

class DashFragment : Fragment() {

    private var _binding: FragmentDashBinding? = null
    private val binding get() = _binding!!

    private val incomeViewModel: IncomeViewModel by viewModels()
    private val expenseViewModel: ExpenseViewModel by viewModels()
    private val budgetViewModel: BudgetViewModel by viewModels()

    private var totalIncome: Double = 0.0
    private var totalExpenses: Double = 0.0
    private var totalBudget: Double = 0.0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUI()
        setOnClickListeners()
        updateCurrentMonth()
        setupViewPagerWithTabs()

        // ✅ Fetch financial data from Firebase
        fetchFinancialData()
    }

    private fun setupUI() {
        binding.balanceAmount.text = "NPR 0.00"
    }

    private fun setOnClickListeners() {
        binding.incomeCard.setOnClickListener {
            val intent = Intent(requireContext(), IncomeActivity::class.java)
            startActivity(intent)
        }

        binding.expensesCard.setOnClickListener {
            val intent = Intent(requireContext(), ExpenseActivity::class.java)
            startActivity(intent)
        }
    }

    private fun fetchFinancialData() {
        val userId = com.google.firebase.auth.FirebaseAuth.getInstance().currentUser?.uid
        if (userId == null) {
            Log.e("DashFragment", "User ID is null")
            return
        }

        // ✅ Fetch Income
        incomeViewModel.fetchIncomes(userId)
        incomeViewModel.incomes.observe(viewLifecycleOwner, Observer { incomes ->
            totalIncome = incomes.sumOf { it.amount.toDouble() }
            updateBalance()
            Log.d("DashFragment", "Total Income: NPR $totalIncome")
        })

        // ✅ Fetch Expenses
        expenseViewModel.fetchExpenses(userId)
        expenseViewModel.expenses.observe(viewLifecycleOwner, Observer { expenses ->
            totalExpenses = expenses.sumOf { it.amount.toDouble() }
            updateBalance()
            Log.d("DashFragment", "Total Expenses: NPR $totalExpenses")
        })

        // ✅ Fetch Budget
        budgetViewModel.getBudgets(userId)
        budgetViewModel.budgets.observe(viewLifecycleOwner, Observer { budgets ->
            totalBudget = budgets.sumOf { it.amount.toDouble() }
            updateBalance()
            Log.d("DashFragment", "Total Budget: NPR $totalBudget")
        })
    }

    private fun updateBalance() {
        val currentBalance = (totalIncome + totalBudget) - totalExpenses
        binding.balanceAmount.text = "NPR ${String.format("%.2f", currentBalance)}"
        Log.d("DashFragment", "Updated Balance: NPR $currentBalance")
    }

    private fun updateCurrentMonth() {
        val dateFormat = SimpleDateFormat("MMMM yyyy", Locale.getDefault())
        val currentMonth = dateFormat.format(Date())
        binding.monthText.text = currentMonth
    }

    private fun setupViewPagerWithTabs() {
        val adapter = DashPagerAdapter(this, requireContext())
        binding.pagerDash.adapter = adapter

        TabLayoutMediator(binding.tabLayout, binding.pagerDash) { tab, position ->
            tab.text = when (position) {
                0 -> "All"
                1 -> "Today"
                2 -> "Week"
                3 -> "Month"
                4 -> "Year"
                else -> null
            }
        }.attach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
