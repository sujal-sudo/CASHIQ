package com.example.cashiq.UI.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cashiq.R
import com.example.cashiq.adapter.TransactionAdapter
import com.example.cashiq.viewmodel.TransactionViewModel
import com.google.firebase.auth.FirebaseAuth

class AllFragment : Fragment() {

    private val transactionViewModel: TransactionViewModel by viewModels()
    private val userId = FirebaseAuth.getInstance().currentUser?.uid

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_all, container, false)

        val recyclerView: RecyclerView = view.findViewById(R.id.all_recycler)
        val loadingIndicator: ImageView = view.findViewById(R.id.loadingIndicator) // ✅ Loading GIF

        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // ✅ Show Loading Animation Before Fetching Data
        loadingIndicator.visibility = View.VISIBLE
        recyclerView.visibility = View.GONE

        loadTransactions(loadingIndicator, recyclerView)

        return view
    }

    // ✅ Fetch Data from ViewModel & Show/Hide Loading Indicator
    private fun loadTransactions(loadingIndicator: ImageView, recyclerView: RecyclerView) {
        userId?.let {
            transactionViewModel.getAllTransactions(it)

            transactionViewModel.transactions.observe(viewLifecycleOwner, Observer { transactions ->
                if (transactions.isNotEmpty()) {
                    recyclerView.adapter = TransactionAdapter(transactions)

                    // ✅ Hide Loading GIF & Show RecyclerView
                    loadingIndicator.visibility = View.GONE
                    recyclerView.visibility = View.VISIBLE
                } else {
                    // ✅ If No Transactions, Still Hide Loading
                    loadingIndicator.visibility = View.GONE
                }
            })
        }
    }
}
