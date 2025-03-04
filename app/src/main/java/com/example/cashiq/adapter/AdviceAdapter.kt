package com.example.cashiq.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cashiq.R

class AdviceAdapter(private val adviceList: List<String>) : RecyclerView.Adapter<AdviceAdapter.AdviceViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdviceViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_advice, parent, false)
        return AdviceViewHolder(view)
    }

    override fun onBindViewHolder(holder: AdviceViewHolder, position: Int) {
        holder.adviceText.text = adviceList[position]
    }

    override fun getItemCount(): Int = adviceList.size

    class AdviceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val adviceText: TextView = itemView.findViewById(R.id.advice_text)
    }
}
