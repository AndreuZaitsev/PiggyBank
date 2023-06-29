package com.example.piggybank.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil.ItemCallback
import androidx.recyclerview.widget.RecyclerView
import com.example.piggybank.databinding.ItemExpenseBinding

class ExpensesStatAdapter() :
    androidx.recyclerview.widget.ListAdapter<ExpensesStatAdapter.ExpenseItem, ExpensesStatAdapter.ExpensesStatViewHolder>(ExpensesStatDiff()) {

    data class ExpenseItem(
        val name: String,
        val date: String,
        val expenseValue: Double
    )

    class ExpensesStatViewHolder(
        private val binding: ItemExpenseBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(expenseItem: ExpenseItem) {
            binding.tvExpenseName.text = expenseItem.name
            binding.tvDateExpense.text = expenseItem.date
            binding.tvExpenseValue.text = expenseItem.expenseValue.toString()
        }
    }

    class ExpensesStatDiff : ItemCallback<ExpenseItem>() {

        override fun areItemsTheSame(oldItem: ExpenseItem, newItem: ExpenseItem): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: ExpenseItem, newItem: ExpenseItem): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpensesStatViewHolder {
        val binding =ItemExpenseBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ExpensesStatViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ExpensesStatViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}