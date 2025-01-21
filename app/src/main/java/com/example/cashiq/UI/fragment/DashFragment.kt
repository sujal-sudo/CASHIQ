package com.example.cashiq.UI.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.example.cashiq.R
import com.example.cashiq.UI.activity.ExpenseActivity
import com.example.cashiq.UI.activity.IncomeActivity
import com.example.cashiq.databinding.FragmentDashBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class DashFragment : Fragment() {

    private var _binding: FragmentDashBinding? = null // Backing property for binding
    private val binding get() = _binding!! // Non-null property for binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout using View Binding
        _binding = FragmentDashBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Call your setup methods
        updateCurrentMonth()
        setupClickListeners()
    }


    private fun updateCurrentMonth() {
        val dateFormat = SimpleDateFormat("MMMM yyyy", Locale.getDefault())
        val currentMonth = dateFormat.format(Date())
        binding.monthText.text = currentMonth
    }

    private fun setupClickListeners() {
        binding.incomeCard.setOnClickListener {
            startActivity(Intent(requireContext(), IncomeActivity::class.java))
        }

        binding.expensesCard.setOnClickListener {
            startActivity(Intent(requireContext(), ExpenseActivity::class.java))
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Avoid memory leaks
    }
}
