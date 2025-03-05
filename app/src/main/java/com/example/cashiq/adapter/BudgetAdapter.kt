package com.example.cashiq.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cashiq.R
import com.example.cashiq.model.BudgetModel

class BudgetAdapter(
    private val budgetList: List<BudgetModel>,
    private val onDeleteClick: (String) -> Unit  // ✅ Added delete function callback
) : RecyclerView.Adapter<BudgetAdapter.BudgetViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BudgetViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_budget, parent, false)
        return BudgetViewHolder(view, onDeleteClick)
    }

    override fun onBindViewHolder(holder: BudgetViewHolder, position: Int) {
        val budget = budgetList[position]
        holder.bind(budget)
    }

    override fun getItemCount(): Int {
        return budgetList.size
    }

    class BudgetViewHolder(itemView: View, private val onDeleteClick: (String) -> Unit) :
        RecyclerView.ViewHolder(itemView) {

        private val categoryTextView: TextView = itemView.findViewById(R.id.budgetCategory)
        private val amountTextView: TextView = itemView.findViewById(R.id.budgetAmount)
        private val startDateTextView: TextView = itemView.findViewById(R.id.budgetStartDate)
        private val endDateTextView: TextView = itemView.findViewById(R.id.budgetEndDate)
        private val deleteButton: ImageButton = itemView.findViewById(R.id.deleteBudgetButton) // ✅ Added Delete Button

        fun bind(budget: BudgetModel) {
            categoryTextView.text = budget.category
            amountTextView.text = "Amount: NPR ${budget.amount}"
            startDateTextView.text = "Start: ${budget.startDate}"
            endDateTextView.text = "End: ${budget.endDate}"

            // ✅ Handle Delete Click
            deleteButton.setOnClickListener {
                onDeleteClick(budget.id)
            }
        }
    }
}
