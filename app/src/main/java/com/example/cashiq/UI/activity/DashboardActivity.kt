package com.example.cashiq.UI.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.cashiq.R
import com.example.cashiq.UI.fragment.ProfileFragment
import com.example.cashiq.databinding.ActivityDashboardBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DashboardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDashboardBinding
    private var totalBalance: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUI()  // Ensure this method is defined

        setupNavigation()
        setupClickListeners()
        updateCurrentMonth()
    }

    private fun setupUI() {
        // Initialize UI components here
        binding.balanceLabel.text = "Your Balance: NPR ${String.format("%.2f", totalBalance)}"
    }

    // Handle the result from IncomeActivity and ExpenseActivity
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                INCOME_REQUEST_CODE -> {
                    val amount = data?.getDoubleExtra("INCOME_AMOUNT", 0.0) ?: 0.0
                    updateBalance(amount)
                }
                EXPENSE_REQUEST_CODE -> {
                    val amount = data?.getDoubleExtra("EXPENSE_AMOUNT", 0.0) ?: 0.0
                    updateBalance(-amount)  // Deduct expense
                }
            }
        }
    }

    private fun updateBalance(amount: Double) {
        totalBalance += amount
        binding.balanceLabel.text = "Your Balance: NPR ${String.format("%.2f", totalBalance)}"
    }

    private fun setupClickListeners() {
        binding.incomeCard.setOnClickListener {
            val intent = Intent(this, IncomeActivity::class.java)
            startActivityForResult(intent, INCOME_REQUEST_CODE)
        }

        binding.expensesCard.setOnClickListener {
            val intent = Intent(this, ExpenseActivity::class.java)
            startActivityForResult(intent, EXPENSE_REQUEST_CODE)
        }

        // Setup time filter clicks...
    }

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

    private fun updateCurrentMonth() {
        val dateFormat = SimpleDateFormat("MMMM yyyy", Locale.getDefault())
        val currentMonth = dateFormat.format(Date())
        binding.monthText.text = currentMonth
    }

    companion object {
        private const val INCOME_REQUEST_CODE = 1
        private const val EXPENSE_REQUEST_CODE = 2
    }
}