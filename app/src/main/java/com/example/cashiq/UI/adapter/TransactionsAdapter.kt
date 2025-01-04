package com.example.cashiq.UI.adapter

import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cashiq.R

class TransactionsAdapter(
    private val transactions: List<Transaction>
) : RecyclerView.Adapter<TransactionsAdapter.TransactionViewHolder>() {

    class TransactionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val icon: ImageView = itemView.findViewById(R.id.transactionIcon)
        private val title: TextView = itemView.findViewById(R.id.transactionTitle)
        private val description: TextView = itemView.findViewById(R.id.transactionDescription)
        private val amount: TextView = itemView.findViewById(R.id.transactionAmount)
        private val time: TextView = itemView.findViewById(R.id.transactionTime)

        fun bind(transaction: Transaction) {
            title.text = transaction.title
            description.text = transaction.description

            val amountText = if (transaction.amount < 0) {
                "- NPR ${abs(transaction.amount)}"
            } else {
                "+ NPR ${transaction.amount}"
            }

            amount.apply {
                text = amountText
                setTextColor(ContextCompat.getColor(context,
                    if (transaction.amount >= 0) R.color.green else R.color.red))
            }

            // Format time as "10:00 AM"
            val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
            time.text = timeFormat.format(Date(transaction.timestamp))

            // Set icon based on transaction type
            icon.setImageResource(
                when {
                    transaction.title.contains("Shopping") -> R.drawable.ic_shopping_cart
                    transaction.title.contains("Salary") -> R.drawable.ic_wallet
                    transaction.title.contains("Bills") -> R.drawable.ic_receipt
                    else -> R.drawable.ic_transaction
                }
            )
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_transaction, parent, false)
        return TransactionViewHolder(view)
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        holder.bind(transactions[position])
    }

    override fun getItemCount() = transactions.size
}

// Update Transaction data class
data class Transaction(
    val title: String,
    val description: String,
    val amount: Int,
    val timestamp: Long
)