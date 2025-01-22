package com.example.cashiq.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cashiq.R
import com.example.cashiq.databinding.ItemTransactionBinding
import com.example.cashiq.model.Transaction

class TransactionAdapter(private val transactions: List<Transaction>) :
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

    class TransactionViewHolder(private val binding: ItemTransactionBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(transaction: Transaction) {
            binding.apply {
                // Set the icon based on income/expense
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

                // Set description and time (you can modify the description logic)
                transactionDescription.text = if (transaction.isIncome) "Income received" else "Expense recorded"
                transactionTime.text = transaction.date

                // Format and set the amount
                transactionAmount.text =
                    if (transaction.isIncome) "+NPR ${String.format("%.2f", transaction.amount)}"
                    else "-NPR ${String.format("%.2f", transaction.amount)}"
            }
        }
    }
}
