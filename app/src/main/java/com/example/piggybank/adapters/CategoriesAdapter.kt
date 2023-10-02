package com.example.piggybank.adapters

import android.app.ProgressDialog.show
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.core.app.DialogCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.piggybank.R
import com.example.piggybank.databinding.ItemCategoryHorBinding
import com.google.android.material.snackbar.Snackbar

class CategoriesAdapter(
    private val onClick: (CategoryItem) -> Unit = {},
    private val onLongClick: (CategoryItem)-> Unit = {}
) : ListAdapter<CategoryItem, CategoryViewHolder>(CategoryDiff()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding = ItemCategoryHorBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bind(getItem(position), onClick, onLongClick)
    }
}

data class CategoryItem(
    val name: String,
    @DrawableRes
    val iconRes: Int,
    val isSelected: Boolean
)

class CategoryDiff : DiffUtil.ItemCallback<CategoryItem>() {

    override fun areItemsTheSame(oldItem: CategoryItem, newItem: CategoryItem): Boolean =
        oldItem.iconRes == newItem.iconRes

    override fun areContentsTheSame(oldItem: CategoryItem, newItem: CategoryItem): Boolean = oldItem == newItem
}

class CategoryViewHolder(
    private val binding: ItemCategoryHorBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: CategoryItem, onClick: (CategoryItem) -> Unit, onLongClick: (CategoryItem) -> Unit) {
        binding.tvName.text = item.name
        binding.ivIcon.setImageResource(item.iconRes)
        binding.ivIcon.background = if (item.isSelected) {
            ContextCompat.getDrawable(binding.root.context, R.drawable.bg_checked)
        } else {
            null
        }
        binding.root.setOnClickListener {
            onClick.invoke(item)
        }
        binding.root.setOnLongClickListener {
            onLongClick.invoke(item)
            true
        }
    }
}