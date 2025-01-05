package com.example.cashiq.UI.activity

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
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
        setupTimeFilters()
        setupTransactionsList()
        setupNavigation()
    }

    private fun setupUI() {
        // Set current month
        val currentMonth = SimpleDateFormat("MMMM", Locale.getDefault()).format(Date())
        binding.monthText.text = currentMonth

        // Setup click listeners for income/expense cards
        binding.incomeCard.setOnClickListener {
            startActivity(Intent(this, IncomeActivity::class.java))
        }

        binding.expensesCard.setOnClickListener {
            startActivity(Intent(this, ExpenseActivity::class.java))
        }

        // Setup notification click
        binding.notificationIcon.setOnClickListener {
            startActivity(Intent(this, NotificationActivity::class.java))
        }

        // Setup see all transactions
        binding.seeAllButton.setOnClickListener {
            // Navigate to all transactions screen
        }
    }

    private fun setupTimeFilters() {
        val filters = listOf(binding.todayFilter, binding.weekFilter,
            binding.monthFilter, binding.yearFilter)

        filters.forEach { filter ->
            filter.setOnClickListener {
                // Reset all filters
                filters.forEach { it.background = null }
                filters.forEach { it.setTextColor(getColor(R.color.gray)) }

                // Highlight selected filter
                filter.background = getDrawable(R.drawable.time_filter_selected_bg)
                filter.setTextColor(getColor(R.color.orange))

                // Update transactions based on selected filter
                updateTransactions(filter.id)
            }
        }
    }

    private fun setupTransactionsList() {
        binding.transactionsRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@DashboardActivity)
            adapter = TransactionsAdapter(getSampleTransactions())
        }
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
            // Set home as selected by default
            selectedItemId = R.id.navHome
        }
    }

    private fun updateTransactions(filterId: Int) {
        // Update transaction list based on selected time filter
        val transactions = when (filterId) {
            R.id.todayFilter -> getSampleTransactions() // Get today's transactions
            R.id.weekFilter -> getSampleTransactions()  // Get weekly transactions
            R.id.monthFilter -> getSampleTransactions() // Get monthly transactions
            R.id.yearFilter -> getSampleTransactions()  // Get yearly transactions
            else -> getSampleTransactions()
        }

        (binding.transactionsRecyclerView.adapter as TransactionsAdapter)
            .updateTransactions(transactions)
    }

    private fun getSampleTransactions(): List<Transaction> {
        return listOf(
            Transaction("Item 1", -500),
            Transaction("Item 2", 1000),
            Transaction("Item 3", -750),
            Transaction("Item 4", -250),
            Transaction("Item 5", 2000)
        )
    }
}

data class Transaction(
    val title: String,
    val amount: Int
)

class TransactionsAdapter(
    private val transactions: List<Transaction>
) : RecyclerView.Adapter<TransactionsAdapter.TransactionViewHolder>() {

    class TransactionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleText: TextView = itemView.findViewById(R.id.transactionTitle)
        private val amountText: TextView = itemView.findViewById(R.id.transactionAmount)

        fun bind(transaction: Transaction) {
            titleText.text = transaction.title
            amountText.apply {
                text = "NPR ${abs(transaction.amount)}"
                setTextColor(
                    ContextCompat.getColor(context,
                    if (transaction.amount >= 0) R.color.green else R.color.red))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_transaction, parent, false)
        return TransactionViewHolder(view)
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        holder.bind(transactions[position])
    }

    override fun getItemCount() = transactions.size
    fun updateTransactions(transactions: List<Transaction>) {

    }
}