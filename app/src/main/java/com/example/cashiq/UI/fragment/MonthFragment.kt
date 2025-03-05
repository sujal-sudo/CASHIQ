package com.example.cashiq.UI.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cashiq.R
import com.example.cashiq.adapter.TransactionAdapter
import com.example.cashiq.viewmodel.TransactionViewModel
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.*

class MonthFragment : Fragment() {

    private val transactionViewModel: TransactionViewModel by viewModels()
    private val userId = FirebaseAuth.getInstance().currentUser?.uid
    private var isCurrentMonth = true // ✅ Toggle between Current & Last Month

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_month, container, false)

        val recyclerView: RecyclerView = view.findViewById(R.id.month_recycler)
        val loadingIndicator: ImageView = view.findViewById(R.id.loadingIndicator)
        val noTransactionsText: TextView = view.findViewById(R.id.noTransactionsText)
        val toggleButton: ToggleButton = view.findViewById(R.id.month_toggle_button) // ✅ Toggle Button

        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // ✅ Show loading indicator before fetching data
        loadingIndicator.visibility = View.VISIBLE
        recyclerView.visibility = View.GONE
        noTransactionsText.visibility = View.GONE

        // ✅ Load Transactions for the Current Month
        loadMonthTransactions(loadingIndicator, recyclerView, noTransactionsText, isCurrentMonth)

        // ✅ Handle Toggle Button Click
        toggleButton.setOnCheckedChangeListener { _, isChecked ->
            isCurrentMonth = !isChecked
            loadMonthTransactions(loadingIndicator, recyclerView, noTransactionsText, isCurrentMonth)
        }

        return view
    }

    // ✅ Fetch Transactions for Selected Month
    private fun loadMonthTransactions(
        loadingIndicator: ImageView,
        recyclerView: RecyclerView,
        noTransactionsText: TextView,
        isCurrent: Boolean
    ) {
        userId?.let {
            transactionViewModel.getAllTransactions(it)

            transactionViewModel.transactions.observe(viewLifecycleOwner, Observer { transactions ->
                Log.d("MonthFragment", "Total Transactions Fetched: ${transactions.size}")

                val selectedMonth = if (isCurrent) getCurrentMonth() else getLastMonth()
                Log.d("MonthFragment", "Selected Month: $selectedMonth")

                val monthTransactions = transactions.filter { transaction ->
                    val transactionMonthOnly = extractMonth(transaction.date)
                    Log.d("MonthFragment", "Transaction Month: $transactionMonthOnly")
                    transactionMonthOnly == selectedMonth
                }

                if (monthTransactions.isNotEmpty()) {
                    val adapter = TransactionAdapter(monthTransactions)
                    recyclerView.adapter = adapter
                    adapter.notifyDataSetChanged()

                    // ✅ Hide Loading & Show Data
                    loadingIndicator.visibility = View.GONE
                    recyclerView.visibility = View.VISIBLE
                    noTransactionsText.visibility = View.GONE
                    Log.d("MonthFragment", "Transactions in selected month: ${monthTransactions.size}")
                } else {
                    // ✅ Show "No Transactions This Month" Message
                    loadingIndicator.visibility = View.GONE
                    recyclerView.visibility = View.GONE
                    noTransactionsText.visibility = View.VISIBLE
                    Log.d("MonthFragment", "No transactions found for selected month.")
                }
            })
        }
    }

    // ✅ Get Current Month in "yyyy-MM" format
    private fun getCurrentMonth(): String {
        val sdf = SimpleDateFormat("yyyy-MM", Locale.getDefault())
        return sdf.format(Date())
    }

    // ✅ Get Last Month in "yyyy-MM" format
    private fun getLastMonth(): String {
        val sdf = SimpleDateFormat("yyyy-MM", Locale.getDefault())
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.MONTH, -1) // Subtract 1 month
        return sdf.format(calendar.time)
    }

    // ✅ Extract "yyyy-MM" from a full date-time string
    private fun extractMonth(fullDate: String): String {
        return fullDate.substring(0, 7) // Extracts "yyyy-MM" from "yyyy-MM-dd HH:mm:ss"
    }
}
