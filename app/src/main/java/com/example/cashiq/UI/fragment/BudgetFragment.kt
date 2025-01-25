package com.example.cashiq.UI.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.cashiq.R
import com.example.cashiq.adapter.AdviceAdapter
import java.text.SimpleDateFormat

class BudgetFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_budget, container, false)

        // Initialize views
        val createBudgetButton: Button = view.findViewById(R.id.create_budget_button)
        val viewPager: ViewPager2 = view.findViewById(R.id.view_pager)

        // Set up swipeable financial advice cards
        val adviceList = listOf(
            "Track your daily expenses to identify spending patterns.",
            "Save at least 20% of your income for emergencies.",
            "Avoid unnecessary subscriptions that you don’t use.",
            "Plan your purchases and avoid impulse buying.",
            "Invest in assets that generate passive income."
        )
        val adapter = AdviceAdapter(adviceList)
        viewPager.adapter = adapter

        // Button click listener to navigate to the budget creation screen
        createBudgetButton.setOnClickListener {
            // Navigate to CreateBudgetFragment
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, CreateBudgetFragment())
                .addToBackStack(null)
                .commit()
        }

        return view
    }
}