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

class YearFragment : Fragment() {

    private val transactionViewModel: TransactionViewModel by viewModels()
    private val userId = FirebaseAuth.getInstance().currentUser?.uid

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_year, container, false)

        val recyclerView: RecyclerView = view.findViewById(R.id.year_recycler)
        val loadingIndicator: ImageView = view.findViewById(R.id.loadingIndicator)
        val noTransactionsText: TextView = view.findViewById(R.id.noTransactionsText)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // ✅ Show loading while fetching data
        loadingIndicator.visibility = View.VISIBLE
        recyclerView.visibility = View.GONE
        noTransactionsText.visibility = View.GONE

        loadYearlyTransactions(loadingIndicator, recyclerView, noTransactionsText)

        return view
    }

    // ✅ Fetch This Year's Transactions
    private fun loadYearlyTransactions(
        loadingIndicator: ImageView,
        recyclerView: RecyclerView,
        noTransactionsText: TextView
    ) {
        userId?.let {
            transactionViewModel.getAllTransactions(it)

            transactionViewModel.transactions.observe(viewLifecycleOwner, Observer { transactions ->
                Log.d("YearFragment", "Total Transactions Fetched: ${transactions.size}")

                val currentYear = getCurrentYear()
                Log.d("YearFragment", "Current Year: $currentYear")

                val yearlyTransactions = transactions.filter { transaction ->
                    val transactionYear = extractYear(transaction.date)
                    Log.d("YearFragment", "Transaction Year: $transactionYear")
                    transactionYear == currentYear
                }

                if (yearlyTransactions.isNotEmpty()) {
                    recyclerView.adapter = TransactionAdapter(yearlyTransactions)

                    // ✅ Hide loading & show transactions
                    loadingIndicator.visibility = View.GONE
                    recyclerView.visibility = View.VISIBLE
                    noTransactionsText.visibility = View.GONE
                    Log.d("YearFragment", "Transactions for this year: ${yearlyTransactions.size}")
                } else {
                    // ✅ Show "No Transactions This Year" Message
                    loadingIndicator.visibility = View.GONE
                    recyclerView.visibility = View.GONE
                    noTransactionsText.visibility = View.VISIBLE
                    Log.d("YearFragment", "No transactions found for this year.")
                }
            })
        }
    }

    // ✅ Get Current Year
    private fun getCurrentYear(): String {
        val sdf = SimpleDateFormat("yyyy", Locale.getDefault())
        return sdf.format(Date())
    }

    // ✅ Extract Year from a full date-time string
    private fun extractYear(fullDate: String): String {
        return fullDate.substring(0, 4) // Extracts "yyyy" from "yyyy-MM-dd HH:mm:ss"
    }
}
