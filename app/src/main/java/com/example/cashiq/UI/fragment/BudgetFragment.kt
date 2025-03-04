package com.example.cashiq.UI.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.viewpager2.widget.ViewPager2
import com.example.cashiq.R
import com.example.cashiq.adapter.AdviceAdapter

class BudgetFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_budget, container, false)

        // Initialize views
        val createBudgetButton: Button = view.findViewById(R.id.create_budget_button)
        val viewPager: ViewPager2 = view.findViewById(R.id.view_pager)

        // Ensure ViewPager2 scrolls properly
        viewPager.offscreenPageLimit = 1

        // Set up swipeable financial advice cards (Shuffled List)
        val adviceList = mutableListOf(
            "Track your daily expenses to identify spending patterns.",
            "Save at least 20% of your income for emergencies.",
            "Avoid unnecessary subscriptions that you don’t use.",
            "Plan your purchases and avoid impulse buying.",
            "Invest in assets that generate passive income."
        ).shuffled() // Randomizing the order

        val adapter = AdviceAdapter(adviceList)
        viewPager.adapter = adapter

        // Ensure ViewPager2 starts from the first position
        viewPager.setCurrentItem(0, false)

        // Button click listener to navigate to the budget creation screen
        createBudgetButton.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, CreateBudgetFragment())
                .addToBackStack(null)
                .commit()
        }

        return view
    }
}
