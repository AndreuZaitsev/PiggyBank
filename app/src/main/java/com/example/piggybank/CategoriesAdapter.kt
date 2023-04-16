package com.example.piggybank

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.piggybank.databinding.ItemCategoryBinding

class CategoriesAdapter() : ListAdapter<CategoryItem, CategoryViewHolder>(CategoryDiff()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding = ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

data class CategoryItem(
    val name: String,
    @DrawableRes val iconRes: Int,
    val isSelected: Boolean
)

class CategoryDiff() : DiffUtil.ItemCallback<CategoryItem>() {

    override fun areItemsTheSame(oldItem: CategoryItem, newItem: CategoryItem): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: CategoryItem, newItem: CategoryItem): Boolean {
        return oldItem == newItem
    }
}

class CategoryViewHolder(
    private val binding: ItemCategoryBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: CategoryItem) {
        binding.tvName.text = item.name
        binding.ivIcon.setImageResource(item.iconRes)
        binding.ivIcon.background = if (item.isSelected) {
            ContextCompat.getDrawable(binding.root.context, R.drawable.bg_checked)
        } else null
    }
}