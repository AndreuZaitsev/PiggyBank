package com.example.piggybank.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil.ItemCallback
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.piggybank.databinding.ItemIncomeBinding

class EditIncomeAdapter(
    private val onClick: (EditIncomeItem) -> Unit = {}
) : ListAdapter<EditIncomeAdapter.EditIncomeItem, EditIncomeAdapter.EditIncomeViewHolder>(EditIncomeDiff()) {

    data class EditIncomeItem(
        val id: Int,
        val name: String = "Income",
        val date: String,
        val incomeValue: Double,
        val isSelected: Boolean = false
    )

    class EditIncomeViewHolder(
        private val binding: ItemIncomeBinding
    ) : RecyclerView.ViewHolder(binding.root), IncomeItemTouchHelperCallback.ItemTouchViewHolder {

        fun bind(incomeItem: EditIncomeItem, onClick: (EditIncomeItem) -> Unit) {
            binding.tvIncomeName.text = incomeItem.name
            binding.tvDateIncome.text = incomeItem.date
            binding.tvIncomeValue.text = incomeItem.incomeValue.toString()
            binding.root.setOnClickListener {
                onClick.invoke(incomeItem)
            }
        }

        override fun onItemCleared() {
            itemView.setBackgroundColor(Color.WHITE)
        }

        override fun onItemSelected() {
            itemView.setBackgroundColor(Color.LTGRAY)
        }
    }

    class EditIncomeDiff : ItemCallback<EditIncomeItem>() {

        override fun areItemsTheSame(oldItem: EditIncomeItem, newItem: EditIncomeItem): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: EditIncomeItem, newItem: EditIncomeItem): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EditIncomeViewHolder {
        val binding = ItemIncomeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EditIncomeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EditIncomeViewHolder, position: Int) {
        holder.bind(getItem(position), onClick)
    }
}