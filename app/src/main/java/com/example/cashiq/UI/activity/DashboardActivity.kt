package com.example.cashiq.UI.activity

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cashiq.R
import com.example.cashiq.UI.fragment.ProfileFragment
import com.example.cashiq.databinding.ActivityDashboardBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.abs

class DashboardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDashboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUI()
//        setupTransactionsList()
        setupNavigation()
        setupClickListeners()
        updateCurrentMonth()
    }

    private fun setupUI() {
        // ... existing setup code ...
    }

    private fun updateCurrentMonth() {
        val dateFormat = SimpleDateFormat("MMMM yyyy", Locale.getDefault())
        val currentMonth = dateFormat.format(Date())
        binding.monthText.text = currentMonth
    }

    private fun setupClickListeners() {
        binding.incomeCard.setOnClickListener {
            startActivity(Intent(this, IncomeActivity::class.java))
        }

        binding.expensesCard.setOnClickListener {
            startActivity(Intent(this, ExpenseActivity::class.java))
        }

        // Setup time filter clicks
        binding.apply {
            val filters = listOf(todayFilter, weekFilter, monthFilter, yearFilter)

            filters.forEach { filter ->
                filter.setOnClickListener {
                    // Reset all filters to default state
                    filters.forEach { it.setBackgroundResource(0) }
                    filters.forEach { it.setTextColor(ContextCompat.getColor(this@DashboardActivity, R.color.gray)) }

                    // Highlight selected filter
                    filter.setBackgroundResource(R.drawable.time_filter_selected_bg)
                    filter.setTextColor(ContextCompat.getColor(this@DashboardActivity, R.color.orange))

                    // Update transactions based on selected filter
//                    updateTransactions(filter.id)
                }
            }
        }
    }

//    private fun setupTransactionsList() {
//        binding.transactionsRecyclerView.layoutManager = LinearLayoutManager(this)
//        binding.transactionsRecyclerView.adapter = TransactionsAdapter(emptyList())
//    }

    private fun setupNavigation() {
        binding.bottomNav.apply {
            setOnItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.navHome -> true
                    R.id.NavProfile -> {
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.container, ProfileFragment())
                            .commit()
                        true
                    }
                    else -> false
                }
            }
            selectedItemId = R.id.navHome
        }
    }

    // ... rest of the existing code ...
}