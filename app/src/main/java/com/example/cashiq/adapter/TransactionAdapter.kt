package com.example.cashiq.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cashiq.R
import com.example.cashiq.databinding.ItemTransactionBinding
import com.example.cashiq.model.Transaction

class TransactionAdapter(private var transactions: List<Transaction>) :
    RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val binding = ItemTransactionBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return TransactionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        holder.bind(transactions[position])
    }

    override fun getItemCount() = transactions.size

    // Method to update data dynamically (used for filtering transactions)
    fun updateData(newTransactions: List<Transaction>) {
        transactions = newTransactions
        notifyDataSetChanged()
    }

    class TransactionViewHolder(private val binding: ItemTransactionBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(transaction: Transaction) {
            binding.apply {
                // Set the icon based on whether it's an income or expense
                val iconRes = if (transaction.isIncome) R.drawable.income else R.drawable.expence
                transactionIcon.setImageResource(iconRes)

                // Set the tint color for the icon (income: green, expense: red)
                transactionIcon.setColorFilter(
                    itemView.context.getColor(
                        if (transaction.isIncome) R.color.green else R.color.red
                    )
                )

                // Set transaction title (e.g., category)
                transactionTitle.text = transaction.category

                // Set the description or fallback to a default based on income/expense
                transactionDescription.text = if (!transaction.description.isNullOrBlank()) {
                    transaction.description
                } else {
                    if (transaction.isIncome) "Income received" else "Expense recorded"
                }

                // Set the date
                transactionTime.text = transaction.date

                // Format and display the amount
                transactionAmount.text = if (transaction.isIncome) {
                    "+NPR ${String.format("%.2f", transaction.amount)}"
                } else {
                    "-NPR ${String.format("%.2f", transaction.amount)}"
                }
            }
        }
    }
}
