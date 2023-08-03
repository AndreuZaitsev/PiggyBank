package com.example.piggybank.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil.ItemCallback
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.piggybank.databinding.EditExpenseItemBinding

class EditExpensesAdapter(
    private val onClick: (EditExpenseItem) -> Unit = {}
) : ListAdapter<EditExpensesAdapter.EditExpenseItem, EditExpensesAdapter.EditExpenseViewHolder>(EditExpenseDiff()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EditExpenseViewHolder {
        val binding = EditExpenseItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EditExpenseViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EditExpenseViewHolder, position: Int) {
        holder.bind(getItem(position), onClick)
    }

    data class EditExpenseItem(
        val id: Int,
        val name: String,
        val date: String,
        val expenseValue: Double,
        val isSelected: Boolean = false
    )

    class EditExpenseViewHolder(
        private val binding: EditExpenseItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(expenseItem: EditExpenseItem, onClick: (EditExpenseItem) -> Unit) {
            binding.tvExpenseName.text = expenseItem.name
            binding.tvDateExpense.text = expenseItem.date
            binding.tvExpenseValue.text = expenseItem.expenseValue.toString()
            binding.root.setOnClickListener {
                onClick.invoke(expenseItem)
            }
        }
    }

    class EditExpenseDiff : ItemCallback<EditExpenseItem>() {

        override fun areItemsTheSame(oldItem: EditExpenseItem, newItem: EditExpenseItem): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: EditExpenseItem, newItem: EditExpenseItem): Boolean {
            return oldItem == newItem
        }
    }
}