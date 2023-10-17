package com.example.piggybank.adapters

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil.ItemCallback
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.piggybank.adapters.MonthlyStatAdapter.MonthlyStatItem
import com.example.piggybank.databinding.ExpenseStatItemBinding

class MonthlyStatAdapter : ListAdapter<MonthlyStatItem, MonthlyStatAdapter.MonthlyStatViewHolder>(MonthlyStatDiff()) {

    data class MonthlyStatItem(
        val expensesColor: Int,
        val expensesName: String,
        val expensesPercent: Double,
        val expensesValue: Float,
    )

    class MonthlyStatViewHolder(
        private val binding: ExpenseStatItemBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(monthlyItem: MonthlyStatItem) {
            binding.ivRect.backgroundTintList = ColorStateList.valueOf(monthlyItem.expensesColor)
            binding.tvExpenseName.text = monthlyItem.expensesName
            binding.itemPercent.text = "${monthlyItem.expensesPercent} %"
            binding.tvExpenseValue.text = monthlyItem.expensesValue.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MonthlyStatViewHolder {
        val binding = ExpenseStatItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MonthlyStatViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MonthlyStatViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class MonthlyStatDiff : ItemCallback<MonthlyStatItem>() {

    override fun areItemsTheSame(
        oldItem: MonthlyStatItem,
        newItem: MonthlyStatItem,
    ): Boolean = oldItem == newItem

    override fun areContentsTheSame(
        oldItem: MonthlyStatItem,
        newItem: MonthlyStatItem,
    ): Boolean = oldItem == newItem
}
