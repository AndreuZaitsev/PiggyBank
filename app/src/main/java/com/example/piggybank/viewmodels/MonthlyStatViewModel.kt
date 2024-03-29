package com.example.piggybank.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.piggybank.ColorfulPalletGenerator
import com.example.piggybank.adapters.StatItem
import com.example.piggybank.repository.ExpensesRepository
import com.example.piggybank.uistates.MonthlyStatUIState
import java.text.SimpleDateFormat
import java.util.Date
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MonthlyStatViewModel @Inject constructor(
    private val repository: ExpensesRepository,
    private val colorfulPallet: ColorfulPalletGenerator,
) : ViewModel() {

    private val _uiState = MutableStateFlow(MonthlyStatUIState())
    val uiState: StateFlow<MonthlyStatUIState> = _uiState.asStateFlow()

    fun onDateSelected(date: Date) {
        showData(date)
    }

    private fun showData(date: Date) {
        viewModelScope.launch {
            _uiState.update { currentState ->
                val expenses = loadData(date.month)
                currentState.copy(
                    expenses = expenses,
                    sumOfExpences = expenses.sumOf { it.expenseValue }.toString(),
                    selectedDate = SimpleDateFormat("MMMM, yyyy").format(date),
                    colors = colorfulPallet.generateColors(expenses.size)
                )
            }
        }
    }

    private suspend fun loadData(month: Int): List<StatItem.ExpenseItem> =
        repository.getExpensesByMonth(month)
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



