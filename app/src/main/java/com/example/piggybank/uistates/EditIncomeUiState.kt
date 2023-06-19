package com.example.piggybank.uistates

import com.example.piggybank.adapters.EditIncomeAdapter

data class EditIncomeUiState(
    val incomes: List<EditIncomeAdapter.EditIncomeItem> = emptyList()
)