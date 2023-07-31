package com.example.piggybank.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.piggybank.adapters.StatItem
import com.example.piggybank.application.DataBaseHolder
import com.example.piggybank.dao.ExpenseEntity
import com.example.piggybank.repository.ExpensesRepository
import com.example.piggybank.uistates.ExpenseStatUiState
import java.text.SimpleDateFormat
import java.util.Locale
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DailyStatViewModel : ViewModel() {

    private val _expenseState = MutableStateFlow(ExpenseStatUiState())
    val expenseState: StateFlow<ExpenseStatUiState> = _expenseState.asStateFlow()

    private val repository = ExpensesRepository(DataBaseHolder.dataBase.expensesDao())

    fun showExpenses() {
        viewModelScope.launch {
            _expenseState.update { currentState ->
                currentState.copy(
                    expenses = loadExpenses()
                )
            }
        }
    }

    private suspend fun loadExpenses(): List<StatItem> =
        repository.getExpenses()
            .groupBy {
                val day = it.dateInMls.toDateItem()
                day
            }
            .flatMap {
                val dateItem = it.key
                val expenseItems = it.value.map { entity -> entity.toItem() }
                val output = mutableListOf<StatItem>()
                output += dateItem
                output += expenseItems
                output.toList()
            }

    private fun ExpenseEntity.toItem(): StatItem.ExpenseItem {
        return StatItem.ExpenseItem(this.name, this.expensesValue.toDouble())
    }

    private fun Long.toDateItem(): StatItem.DateItem {
        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.ROOT)
        val uiDate = dateFormat.format(this)
        return StatItem.DateItem(uiDate)
    }
}