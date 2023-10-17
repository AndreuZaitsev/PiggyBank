package com.example.piggybank.adapters

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil.ItemCallback
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.piggybank.adapters.YearStatAdapter.YearStatItem
import com.example.piggybank.adapters.YearStatAdapter.YearStatViewHolder
import com.example.piggybank.databinding.ExpenseStatItemBinding

class YearStatAdapter : ListAdapter<YearStatItem, YearStatViewHolder>(YearStatDiff()) {

    data class YearStatItem(
        val expensesColor: Int,
        val expensesName: String,
        val expensesPercent: Double,
        val expensesValue: Float,
    )

    class YearStatViewHolder(
        private val binding: ExpenseStatItemBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(yearItem: YearStatItem) {
            binding.ivRect.backgroundTintList = ColorStateList.valueOf(yearItem.expensesColor)
            binding.tvExpenseName.text = yearItem.expensesName
            binding.itemPercent.text = "${yearItem.expensesPercent}%"
            binding.tvExpenseValue.text = yearItem.expensesValue.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): YearStatViewHolder {
        val binding = ExpenseStatItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return YearStatViewHolder(binding)
    }

    override fun onBindViewHolder(holder: YearStatViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class YearStatDiff : ItemCallback<YearStatItem>() {

    override fun areItemsTheSame(oldItem: YearStatItem, newItem: YearStatItem) =
        oldItem == newItem

    override fun areContentsTheSame(oldItem: YearStatItem, newItem: YearStatItem) =
        oldItem == newItem
}
