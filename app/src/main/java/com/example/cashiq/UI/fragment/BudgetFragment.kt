package com.example.cashiq.UI.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_budget, container, false)

        val recyclerView: RecyclerView = view.findViewById(R.id.budget_list)
        val createBudgetButton: Button = view.findViewById(R.id.create_budget_button)
        val fragmentContainer: View = view.findViewById(R.id.fragment_container)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // ✅ Load Budgets
        loadBudgets()

        budgetViewModel.budgets.observe(viewLifecycleOwner, Observer { budgets ->
            recyclerView.adapter = BudgetAdapter(budgets) { budgetId ->
                showDeleteConfirmationDialog(budgetId)
            }
        })

        // ✅ Open CreateBudgetFragment on Button Click
        createBudgetButton.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, CreateBudgetFragment.newInstance {
                    refreshBudgetList() // ✅ Refresh after adding
                })
                .addToBackStack(null)
                .commit()
        }

        return view
    }

    // ✅ Load Budget Data from Firebase
    private fun loadBudgets() {
        userId?.let { budgetViewModel.getBudgets(it) }
    }

    // ✅ Refresh Budget List after Adding/Deleting
    private fun refreshBudgetList() {
        userId?.let { budgetViewModel.getBudgets(it) }
    }

    // ✅ Show Confirmation Dialog Before Deleting a Budget
    private fun showDeleteConfirmationDialog(budgetId: String) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Delete Budget")
        builder.setMessage("Are you sure you want to delete this budget?")

        builder.setPositiveButton("Delete") { _, _ ->
            userId?.let {
                budgetViewModel.deleteBudget(budgetId, it)
                Toast.makeText(requireContext(), "Budget deleted successfully", Toast.LENGTH_SHORT).show()
            }
        }

        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.dismiss() // Close dialog without deleting
        }

        val dialog = builder.create()
        dialog.show()
    }
}
