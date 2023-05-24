package com.example.piggybank.uistates

import com.example.piggybank.adapters.CategoryItem

data class MainUiState(
    val balance: String = "0.0",
    val categories: List<CategoryItem> = emptyList(),
    val keyboardInput: String = ""
)