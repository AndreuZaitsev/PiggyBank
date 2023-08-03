package com.example.piggybank.uistates

import com.example.piggybank.adapters.EditExpensesAdapter

data class EditExpensesUiState (
    val expenses: List<EditExpensesAdapter.EditExpenseItem> = emptyList()
                               )