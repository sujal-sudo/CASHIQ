package com.example.cashiq.UI.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.cashiq.R
import com.example.cashiq.UI.activity.ExpenseActivity
import com.example.cashiq.UI.activity.IncomeActivity
import com.example.cashiq.databinding.FragmentDashBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DashFragment : Fragment() {

    private var _binding: FragmentDashBinding? = null
    private val binding get() = _binding!!
    private var totalBalance: Double = 0.0

    // Register activity result launchers
    private val incomeLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == AppCompatActivity.RESULT_OK) {
            val amount = result.data?.getDoubleExtra("INCOME_AMOUNT", 0.0) ?: 0.0
            updateBalance(amount)
        }
    }

    private val expenseLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
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
//        loadRecyclerViewLoaderIntoScrollView()
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

        binding.apply {
            val filters = listOf(todayFilter, weekFilter, monthFilter, yearFilter)

            filters.forEach { filter ->
                filter.setOnClickListener {
                    // Reset all filters to default state
                    filters.forEach { it.setBackgroundResource(0) }
                    filters.forEach { it.setTextColor(ContextCompat.getColor(requireContext(), R.color.gray)) }

                    // Highlight selected filter
                    filter.setBackgroundResource(R.drawable.time_filter_selected_bg)
                    filter.setTextColor(ContextCompat.getColor(requireContext(), R.color.orange))

                    // Update transactions based on selected filter
                    // updateTransactions(filter.id)
                }
            }
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

//    private fun loadRecyclerViewLoaderIntoScrollView() {
//        // Get the LinearLayout inside the HorizontalScrollView
//        val transactionContainer = binding.TransactionScroolViewDash.getChildAt(0) as LinearLayout
//        transactionContainer.removeAllViews() // Clear any existing views if necessary
//
//        // Inflate the recycler_view_loader_dashfrag layout
//        val inflater = LayoutInflater.from(requireContext())
//        val recyclerViewLoaderView = inflater.inflate(R.layout.recycler_view_loader_dashfrag, transactionContainer, false)
//
//        // Add the inflated view to the container
//        transactionContainer.addView(recyclerViewLoaderView)
//    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Avoid memory leaks
    }

    companion object {
        const val INCOME_REQUEST_CODE = 1001
        const val EXPENSE_REQUEST_CODE = 1002
    }
}
