package com.example.piggybank.uistates

import com.example.piggybank.adapters.ExpensesStatAdapter

data class ExpenseStatUiState(
    val expenses: List<ExpensesStatAdapter> = emptyList()
)