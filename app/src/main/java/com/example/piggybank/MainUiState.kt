package com.example.piggybank

data class MainUiState(
    val balance: String = "0.0",
    val categories: List<CategoryItem> = emptyList(),
    val keyboardInput: String = ""
)