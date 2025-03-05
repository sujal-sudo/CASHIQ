package com.example.cashiq.UI.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cashiq.R
import com.example.cashiq.adapter.BudgetAdapter
import com.example.cashiq.viewmodel.BudgetViewModel
import com.google.firebase.auth.FirebaseAuth

class BudgetFragment : Fragment() {

    private val budgetViewModel: BudgetViewModel by viewModels()
    private val userId = FirebaseAuth.getInstance().currentUser?.uid
    private var isCreatingBudget = false // Keeps track of whether Create Budget Fragment is open

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_budget, container, false)

        val recyclerView: RecyclerView = view.findViewById(R.id.budget_list)
        val createBudgetButton: Button = view.findViewById(R.id.create_budget_button)
        val fragmentContainer: View = view.findViewById(R.id.fragment_container)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Fetch Budgets
        loadBudgets()

        // Observe Budget Data & Update RecyclerView
        budgetViewModel.budgets.observe(viewLifecycleOwner, Observer { budgets ->
            recyclerView.adapter = BudgetAdapter(budgets)
        })

        // ✅ Button Click Listener to Open Create Budget Fragment
        createBudgetButton.setOnClickListener {
            openCreateBudgetFragment()
        }

        return view
    }

    // ✅ Load Budget Data from Firebase
    private fun loadBudgets() {
        if (userId != null) {
            budgetViewModel.getBudgets(userId)
        }
    }

// ✅ Opens CreateBudgetFragment & Hides Budget List
    private fun openCreateBudgetFragment() {
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, CreateBudgetFragment.newInstance {
                closeCreateBudgetFragment()  // ✅ Callback when budget is added
            })
            .addToBackStack(null)
            .commit()

        view?.findViewById<RecyclerView>(R.id.budget_list)?.visibility = View.GONE // Hide RecyclerView
        view?.findViewById<View>(R.id.fragment_container)?.visibility = View.VISIBLE // Show Fragment Container
    }


    // ✅ Closes CreateBudgetFragment After Budget is Added & Refreshes List
    private fun closeCreateBudgetFragment() {
        parentFragmentManager.popBackStack()  // Close fragment

        // Show RecyclerView again
        view?.findViewById<RecyclerView>(R.id.budget_list)?.visibility = View.VISIBLE
        view?.findViewById<View>(R.id.fragment_container)?.visibility = View.GONE

        // Refresh Budget List
        budgetViewModel.getBudgets(userId ?: "")
    }
}