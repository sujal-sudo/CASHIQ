package com.example.cashiq.UI.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cashiq.R
import com.example.cashiq.UI.activity.ExpenseActivity
import com.example.cashiq.UI.activity.IncomeActivity
import com.example.cashiq.adapter.TransactionAdapter
import com.example.cashiq.databinding.FragmentDashBinding
import com.example.cashiq.model.IncomeExpense
import com.example.cashiq.model.Transaction
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.*

class DashFragment : Fragment() {

    private var _binding: FragmentDashBinding? = null
    private val binding get() = _binding!!

    private var totalBalance: Double = 0.0
    private val transactions = mutableListOf<Transaction>()
    private lateinit var transactionAdapter: TransactionAdapter

    // Register activity result launchers
    private val incomeLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == AppCompatActivity.RESULT_OK) {
            val amount = result.data?.getDoubleExtra("INCOME_AMOUNT", 0.0) ?: 0.0
            val category = result.data?.getStringExtra("CATEGORY") ?: "Other"
            addTransaction(amount, category, isIncome = true)
        }
    }

    private val expenseLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == AppCompatActivity.RESULT_OK) {
            val amount = result.data?.getDoubleExtra("EXPENSE_AMOUNT", 0.0) ?: 0.0
            val category = result.data?.getStringExtra("CATEGORY") ?: "Other"
            addTransaction(-amount, category, isIncome = false)
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
        setupRecyclerView()
        setOnClickListeners()
        updateCurrentMonth()
        loadUserData()
    }

    private fun setupUI() {
        binding.balanceAmount.text = "NPR ${String.format("%.2f", totalBalance)}"
    }

    private fun setupRecyclerView() {
        transactionAdapter = TransactionAdapter(transactions)
        binding.transactionsRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = transactionAdapter
        }
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
                    filters.forEach { it.setBackgroundResource(0) }
                    filters.forEach { it.setTextColor(ContextCompat.getColor(requireContext(), R.color.gray)) }
                    filter.setBackgroundResource(R.drawable.time_filter_selected_bg)
                    filter.setTextColor(ContextCompat.getColor(requireContext(), R.color.orange))
                }
            }
        }
    }

    private fun loadUserData() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId == null) {
            Toast.makeText(requireContext(), "User not logged in", Toast.LENGTH_SHORT).show()
            return
        }

        val userRef = FirebaseDatabase.getInstance().getReference("users/$userId")

        // Load income data
        userRef.child("income").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val incomeList = snapshot.children.mapNotNull { it.getValue(IncomeExpense::class.java) }
                // Handle UI updates for incomeList if needed
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), "Failed to load income data", Toast.LENGTH_SHORT).show()
            }
        })

        // Load expense data
        userRef.child("expenses").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val expenseList = snapshot.children.mapNotNull { it.getValue(IncomeExpense::class.java) }
                // Handle UI updates for expenseList if needed
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), "Failed to load expense data", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun addTransaction(amount: Double, category: String, isIncome: Boolean) {
        totalBalance += amount
        binding.balanceAmount.text = "NPR ${String.format("%.2f", totalBalance)}"

        val transaction = Transaction(
            amount = amount,
            category = category,
            date = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date()),
            isIncome = isIncome
        )
        transactions.add(0, transaction)
        transactionAdapter.notifyItemInserted(0)
        binding.transactionsRecyclerView.scrollToPosition(0)
    }

    private fun updateCurrentMonth() {
        val dateFormat = SimpleDateFormat("MMMM yyyy", Locale.getDefault())
        val currentMonth = dateFormat.format(Date())
        binding.monthText.text = currentMonth
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

