package com.example.piggybank.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil.ItemCallback
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.piggybank.R
import com.example.piggybank.adapters.AddCategoriesAdapter.AddCategoryItem
import com.example.piggybank.adapters.AddCategoriesAdapter.AddCategoryViewHolder
import com.example.piggybank.databinding.ItemCategoryVertBinding

class AddCategoriesAdapter(
    private val onClick: (AddCategoryItem) -> Unit = {}
) : ListAdapter<AddCategoryItem, AddCategoryViewHolder>(AddCategoryDiff()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddCategoryViewHolder {
        val binding = ItemCategoryVertBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AddCategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AddCategoryViewHolder, position: Int) {
        holder.bind(getItem(position), onClick)
    }

    data class AddCategoryItem(
        val name: String,
        @DrawableRes val iconRes: Int,
        val isSelected: Boolean = false
    )

    class AddCategoryDiff : ItemCallback<AddCategoryItem>() {

        override fun areContentsTheSame(oldItem: AddCategoryItem, newItem: AddCategoryItem): Boolean {
            return oldItem == newItem
        }

        override fun areItemsTheSame(oldItem: AddCategoryItem, newItem: AddCategoryItem): Boolean {
            return oldItem == newItem
        }
    }

    class AddCategoryViewHolder(
        private val binding: ItemCategoryVertBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: AddCategoryItem, onClick: (AddCategoryItem) -> Unit) {
            binding.tvName.text = item.name
            binding.ivIcon.setImageResource(item.iconRes)
            binding.ivIcon.background = if (item.isSelected) {
                ContextCompat.getDrawable(binding.root.context, R.drawable.bg_checked)
            } else null
            binding.root.setOnClickListener {
                onClick.invoke(item)
            }
        }
    }
}