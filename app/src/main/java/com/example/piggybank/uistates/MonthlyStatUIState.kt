package com.example.piggybank.uistates

import com.example.piggybank.adapters.MonthlyStatAdapter
import com.example.piggybank.adapters.StatItem

data class MonthlyStatUIState(
    val expenses: List<StatItem.ExpenseItem> = emptyList(),
    val sumOfExpences: String = "",
    val selectedDate: String = "",
    val colors: List<Int> = emptyList()
)