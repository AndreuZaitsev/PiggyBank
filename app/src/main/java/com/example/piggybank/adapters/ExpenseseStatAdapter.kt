package com.example.piggybank.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil.ItemCallback
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.piggybank.databinding.DataExpenseBinding
import com.example.piggybank.databinding.ItemExpenseBinding

class ExpensesStatAdapter() :
    ListAdapter<StatItem, RecyclerView.ViewHolder>(StatItemDiff()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            HEADER -> DateStatViewHolder(DataExpenseBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            EXPENSES -> ExpensesStatViewHolder(ItemExpenseBinding.inflate(LayoutInflater.from(parent.context),
                parent, false))
            else -> throw java.lang.ClassCastException("Unknown type: $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is DateStatViewHolder -> holder.bind(getItem(position) as StatItem.DateItem)
            is ExpensesStatViewHolder -> holder.bind(getItem(position) as StatItem.ExpenseItem)
        }
    }

    companion object {

        private const val HEADER = 1
        private const val EXPENSES = 2
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is StatItem.DateItem -> HEADER
            is StatItem.ExpenseItem -> EXPENSES
        }
    }

    class ExpensesStatViewHolder(
        private val binding: ItemExpenseBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(expenseItem: StatItem.ExpenseItem) {
            binding.tvExpenseName.text = expenseItem.name
            binding.tvExpenseValue.text = expenseItem.expenseValue.toString()
        }
    }

    class DateStatViewHolder(
        private val binding: DataExpenseBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(dateItem: StatItem.DateItem) {
            binding.tvDateExpense.text = dateItem.date
        }
    }

    class StatItemDiff : ItemCallback<StatItem>() {

        override fun areItemsTheSame(oldItem: StatItem, newItem: StatItem): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: StatItem, newItem: StatItem): Boolean {
            return oldItem == newItem
        }
    }
}

sealed class StatItem {
    data class DateItem(
        val date: String,
    ) : StatItem()

    data class ExpenseItem(
        val name: String,
        val expenseValue: Double
    ) : StatItem()
}