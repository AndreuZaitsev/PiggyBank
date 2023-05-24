package com.example.piggybank.uistates

import com.example.piggybank.adapters.AddCategoriesAdapter.AddCategoryItem

data class AddUiState(
    val categories: List<AddCategoryItem> = emptyList()
)