package com.example.piggybank.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.piggybank.ColorfulPalletGenerator
import com.example.piggybank.adapters.StatItem
import com.example.piggybank.application.DataBaseHolder
import com.example.piggybank.repository.ExpensesRepository
import com.example.piggybank.uistates.YearStatUIState
import java.text.SimpleDateFormat
import java.util.Date
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class YearStatViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(YearStatUIState())
    val uiState: StateFlow<YearStatUIState> = _uiState.asStateFlow()

    private val repository = ExpensesRepository(DataBaseHolder.dataBase.expensesDao())

    private val colorfulPallet = ColorfulPalletGenerator()

    fun onDateSelected(date: Date) {
        showData(date)
    }

    private fun showData(date: Date) {
        viewModelScope.launch {
            _uiState.update { currentState ->
                val expenses = loadData(date.year)
                currentState.copy(
                    expenses = expenses,
                    sumOfExpences = expenses.sumOf { it.expenseValue }.toString(),
                    selectedDate = SimpleDateFormat("yyyy").format(date),
                    colors = colorfulPallet.generateColors(expenses.size)
                )
            }
        }
    }

    private suspend fun loadData(year: Int): List<StatItem.ExpenseItem> =
        repository.getExpensesByYear(year)
            .groupBy {
                it.categoryName
            }
            .mapValues {
                val sumByCategory = it.value.sumOf { num -> num.expensesValue.toDouble() }
                sumByCategory
            }
            .toList()
            .map { StatItem.ExpenseItem(it.first, it.second) }
}

