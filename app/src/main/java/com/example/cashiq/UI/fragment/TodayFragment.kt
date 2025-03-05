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

class TodayFragment : Fragment() {

    private val transactionViewModel: TransactionViewModel by viewModels()
    private val userId = FirebaseAuth.getInstance().currentUser?.uid

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_today, container, false)

        val recyclerView: RecyclerView = view.findViewById(R.id.today_recycler)
        val loadingIndicator: ImageView = view.findViewById(R.id.loadingIndicator)
        val noTransactionsText: TextView = view.findViewById(R.id.noTransactionsText)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // ✅ Show Loading Indicator Before Fetching Data
        loadingIndicator.visibility = View.VISIBLE
        recyclerView.visibility = View.GONE
        noTransactionsText.visibility = View.GONE

        loadTodayTransactions(loadingIndicator, recyclerView, noTransactionsText)

        return view
    }

    // ✅ Fetch Today's Transactions & Fix Date Filtering
    private fun loadTodayTransactions(loadingIndicator: ImageView, recyclerView: RecyclerView, noTransactionsText: TextView) {
        userId?.let {
            transactionViewModel.getAllTransactions(it)

            transactionViewModel.transactions.observe(viewLifecycleOwner, Observer { transactions ->
                Log.d("TodayFragment", "Total Transactions Fetched: ${transactions.size}")

                val today = getCurrentDate()
                Log.d("TodayFragment", "Today's Date: $today")

                val todayTransactions = transactions.filter { transaction ->
                    val transactionDateOnly = extractDate(transaction.date)
                    Log.d("TodayFragment", "Transaction Date (Extracted): $transactionDateOnly")
                    Log.d("TodayFragment", "Comparison: ${transactionDateOnly == today}") // ✅ Log this for debugging
                    transactionDateOnly == today
                }

                if (todayTransactions.isNotEmpty()) {
                    val adapter = TransactionAdapter(todayTransactions)
                    recyclerView.adapter = adapter
                    adapter.notifyDataSetChanged() // ✅ Ensure data refresh

                    // ✅ Hide Loading & Show Data
                    loadingIndicator.visibility = View.GONE
                    recyclerView.visibility = View.VISIBLE
                    noTransactionsText.visibility = View.GONE
                    Log.d("TodayFragment", "Transactions displayed: ${todayTransactions.size}")
                } else {
                    // ✅ Show "No Transactions Today" Message
                    loadingIndicator.visibility = View.GONE
                    recyclerView.visibility = View.GONE
                    noTransactionsText.visibility = View.VISIBLE
                    Log.d("TodayFragment", "No transactions found for today.")
                }
            })
        }
    }


    // ✅ Get Current Date in "yyyy-MM-dd" format
    private fun getCurrentDate(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return sdf.format(Date())
    }

    // ✅ Extract only "yyyy-MM-dd" from a full date-time string
    private fun extractDate(fullDate: String): String {
        return fullDate.substring(0, 10) // Extracts "yyyy-MM-dd" from "yyyy-MM-dd HH:mm:ss"
    }
}
