package com.example.piggybank.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.piggybank.adapters.ExpensesStatAdapter
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

class ExpensesObject1ViewModel : ViewModel() {

    private val _expenseState = MutableStateFlow(ExpenseStatUiState())
    val expenseState: StateFlow<ExpenseStatUiState> = _expenseState.asStateFlow()

    private val repository = ExpensesRepository(DataBaseHolder.dataBase.expensesDao())


    fun showExpenses(){
        viewModelScope.launch {
            _expenseState.update { currentState->
                currentState.copy(
                    expenses = loadExpenses()
                )
            }
        }
    }
    private suspend fun loadExpenses(): List<ExpensesStatAdapter.ExpenseItem> =
        repository.getExpenses().map { it.toItem() }

    private fun ExpensesStatAdapter.ExpenseItem.toEntity(): ExpenseEntity {
        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.ROOT)
        val timeMillis = dateFormat.parse(this.date)?.time ?: 0L
        return ExpenseEntity(timeMillis, this.name, this.expenseValue.toString())
    }

    private fun ExpenseEntity.toItem(): ExpensesStatAdapter.ExpenseItem {
        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.ROOT)
        val uiDate = dateFormat.format(this.dateInMls)
        return ExpensesStatAdapter.ExpenseItem(this.name, uiDate, this.expensesValue.toDouble())
    }
}