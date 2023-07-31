package com.example.piggybank.uistates

import com.example.piggybank.adapters.ExpensesStatAdapter
import com.example.piggybank.adapters.StatItem

data class ExpenseStatUiState(
    val expenses: List<StatItem> = emptyList()
)