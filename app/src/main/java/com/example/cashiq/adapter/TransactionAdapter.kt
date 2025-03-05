package com.example.cashiq.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cashiq.R
import com.example.cashiq.model.TransactionModel

class TransactionAdapter(private val transactionList: List<TransactionModel>) :
    RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_transaction, parent, false)
        return TransactionViewHolder(view)
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        val transaction = transactionList[position]
        holder.bind(transaction)
    }

    override fun getItemCount(): Int {
        return transactionList.size
    }

    class TransactionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val transactionIcon: ImageView = itemView.findViewById(R.id.transactionIcon)
        private val transactionTitle: TextView = itemView.findViewById(R.id.transactionTitle)
        private val transactionDescription: TextView = itemView.findViewById(R.id.transactionDescription)
        private val transactionAmount: TextView = itemView.findViewById(R.id.transactionAmount)
        private val transactionTime: TextView = itemView.findViewById(R.id.transactionTime)

        fun bind(transaction: TransactionModel) {
            transactionTitle.text = transaction.category  // ✅ Set Category as Title
            transactionDescription.text = transaction.note // ✅ Set Note as Description
            transactionAmount.text = "NPR ${transaction.amount}" // ✅ Set Amount
            transactionTime.text = transaction.date // ✅ Set Date

            // ✅ Change Icon Based on Transaction Type
            if (transaction.type == "income") {
                transactionIcon.setImageResource(R.drawable.income) // Use income.png from drawable
            } else {
                transactionIcon.setImageResource(R.drawable.expence) // Use expense.png from drawable
            }
        }
    }
}
