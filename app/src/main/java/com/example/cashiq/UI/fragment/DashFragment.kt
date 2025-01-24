package com.example.cashiq.UI.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.cashiq.R
import com.example.cashiq.UI.activity.ExpenseActivity
import com.example.cashiq.UI.activity.IncomeActivity
import com.example.cashiq.databinding.FragmentDashBinding
import com.google.android.material.tabs.TabLayoutMediator
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DashFragment : Fragment() {

    private var _binding: FragmentDashBinding? = null
    private val binding get() = _binding!!
    private var totalBalance: Double = 0.0

    // Register activity result launchers
    private val incomeLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == AppCompatActivity.RESULT_OK) {
                val amount = result.data?.getDoubleExtra("INCOME_AMOUNT", 0.0) ?: 0.0
                updateBalance(amount)
            }
        }

    private val expenseLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == AppCompatActivity.RESULT_OK) {
                val amount = result.data?.getDoubleExtra("EXPENSE_AMOUNT", 0.0) ?: 0.0
                updateBalance(-amount)
            }
        }

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
    }

    private fun setupUI() {
        binding.balanceAmount.text = "NPR ${String.format("%.2f", totalBalance)}"
    }

    private fun setOnClickListeners() {
        binding.incomeCard.setOnClickListener {
            val intent = Intent(requireContext(), IncomeActivity::class.java)
            incomeLauncher.launch(intent)
        }

        binding.expensesCard.setOnClickListener {
            val intent = Intent(requireContext(), ExpenseActivity::class.java)
            expenseLauncher.launch(intent)
        }
    }

    private fun updateBalance(amount: Double) {
        totalBalance += amount
        binding.balanceAmount.text = "NPR ${String.format("%.2f", totalBalance)}"
    }

    private fun updateCurrentMonth() {
        val dateFormat = SimpleDateFormat("MMMM yyyy", Locale.getDefault())
        val currentMonth = dateFormat.format(Date())
        binding.monthText.text = currentMonth
    }

    private fun setupViewPagerWithTabs() {
        // Set up the adapter for ViewPager2
        val adapter = DashPagerAdapter(this)
        binding.pagerDash.adapter = adapter

        // Connect TabLayout with ViewPager2
        TabLayoutMediator(binding.tabLayout, binding.pagerDash) { tab, position ->
            tab.text = when (position) {
                0 -> "Today"
                1 -> "Week"
                2 -> "Month"
                3 -> "Year"
                else -> null
            }
        }.attach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Avoid memory leaks
    }

    companion object {
        const val INCOME_REQUEST_CODE = 1001
        const val EXPENSE_REQUEST_CODE = 1002
    }
}
