package com.example.piggybank.uistates

import com.example.piggybank.adapters.StatItem

data class DailyStatUIState(
    val expenses: List<StatItem> = emptyList()
)