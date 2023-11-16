package com.example.piggybank.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.piggybank.adapters.StatItem
import com.example.piggybank.dao.ExpenseEntity
import com.example.piggybank.repository.ExpensesRepository
import com.example.piggybank.uistates.DailyStatUIState
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DailyStatViewModel @Inject constructor(
    private val repository: ExpensesRepository
) : ViewModel() {

    private val _dailyState = MutableStateFlow(DailyStatUIState())
    val dailyState: StateFlow<DailyStatUIState> = _dailyState.asStateFlow()

    fun showExpenses() {
        viewModelScope.launch {
            _dailyState.update { currentState ->
                val expenses = loadExpenses()
                currentState.copy(
                    expenses = expenses
                )
            }
        }
    }

    private suspend fun loadExpenses(): List<StatItem> =
        repository.getExpenses()
            .groupBy {
                it.dateInMls.toDateItem()
            }
            .flatMap {
                val dateItem = it.key
                val expenseItems = it.value
                    .map { entity -> entity.toItem() }
                    .groupBy { expenseItem ->
                        expenseItem.name
                    }.mapValues { expenseValue ->
                        val sumByCategory = expenseValue
                            .value
                            .sumOf { num -> num.expenseValue }
                        sumByCategory
                    }
                    .toList()
                    .map { expenseItem ->
                        StatItem.ExpenseItem(expenseItem.first, expenseItem.second)
                    }
                val output = mutableListOf<StatItem>()
                output += dateItem
                output += expenseItems
                output.toList()
            }

    private fun ExpenseEntity.toItem(): StatItem.ExpenseItem {
        return StatItem.ExpenseItem(this.categoryName, this.expensesValue.toDouble())
    }

    private fun Long.toDateItem(): StatItem.DateItem {
        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.ROOT)
        val uiDate = dateFormat.format(this)
        return StatItem.DateItem(uiDate)
    }
}