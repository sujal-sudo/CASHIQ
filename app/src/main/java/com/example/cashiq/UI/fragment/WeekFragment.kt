package com.example.cashiq.UI.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
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

class WeekFragment : Fragment() {

    private val transactionViewModel: TransactionViewModel by viewModels()
    private val userId = FirebaseAuth.getInstance().currentUser?.uid

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_week, container, false)

        val recyclerView: RecyclerView = view.findViewById(R.id.week_recycler)
        val loadingIndicator: ImageView = view.findViewById(R.id.loadingIndicator)
        val noTransactionsText: TextView = view.findViewById(R.id.noTransactionsText)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // ✅ Show loading indicator while fetching data
        loadingIndicator.visibility = View.VISIBLE
        recyclerView.visibility = View.GONE
        noTransactionsText.visibility = View.GONE

        loadWeekTransactions(loadingIndicator, recyclerView, noTransactionsText)

        return view
    }

    // ✅ Fetch Transactions for the Current Week
    private fun loadWeekTransactions(
        loadingIndicator: ImageView,
        recyclerView: RecyclerView,
        noTransactionsText: TextView
    ) {
        userId?.let {
            transactionViewModel.getAllTransactions(it)

            transactionViewModel.transactions.observe(viewLifecycleOwner, Observer { transactions ->
                Log.d("WeekFragment", "Total Transactions Fetched: ${transactions.size}")

                val (weekStart, weekEnd) = getCurrentWeekRange()
                Log.d("WeekFragment", "Week Start: $weekStart, Week End: $weekEnd")

                val weekTransactions = transactions.filter { transaction ->
                    val transactionDateOnly = extractDate(transaction.date)
                    Log.d("WeekFragment", "Transaction Date: $transactionDateOnly")
                    transactionDateOnly in weekStart..weekEnd
                }

                if (weekTransactions.isNotEmpty()) {
                    val adapter = TransactionAdapter(weekTransactions)
                    recyclerView.adapter = adapter
                    adapter.notifyDataSetChanged()

                    // ✅ Hide Loading & Show Data
                    loadingIndicator.visibility = View.GONE
                    recyclerView.visibility = View.VISIBLE
                    noTransactionsText.visibility = View.GONE
                    Log.d("WeekFragment", "Transactions this week: ${weekTransactions.size}")
                } else {
                    // ✅ Show "No Transactions This Week" Message
                    loadingIndicator.visibility = View.GONE
                    recyclerView.visibility = View.GONE
                    noTransactionsText.visibility = View.VISIBLE
                    Log.d("WeekFragment", "No transactions found for this week.")
                }
            })
        }
    }

    // ✅ Get Current Week Start & End Dates in "yyyy-MM-dd" format
    private fun getCurrentWeekRange(): Pair<String, String> {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val calendar = Calendar.getInstance()

        // Get the first day of the week (Monday)
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
        val weekStart = sdf.format(calendar.time)

        // Get the last day of the week (Sunday)
        calendar.add(Calendar.DAY_OF_WEEK, 6)
        val weekEnd = sdf.format(calendar.time)

        return Pair(weekStart, weekEnd)
    }

    // ✅ Extract only "yyyy-MM-dd" from a full date-time string
    private fun extractDate(fullDate: String): String {
        return fullDate.substring(0, 10) // Extracts "yyyy-MM-dd" from "yyyy-MM-dd HH:mm:ss"
    }
}
