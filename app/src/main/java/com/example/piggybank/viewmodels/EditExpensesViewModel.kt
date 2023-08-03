package com.example.piggybank.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.piggybank.adapters.EditExpensesAdapter
import com.example.piggybank.adapters.EditIncomeAdapter
import com.example.piggybank.application.DataBaseHolder
import com.example.piggybank.dao.ExpenseEntity
import com.example.piggybank.dao.IncomeEntity
import com.example.piggybank.repository.ExpensesRepository
import com.example.piggybank.uistates.EditExpensesUiState
import java.text.SimpleDateFormat
import java.util.Locale
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EditExpensesViewModel: ViewModel() {

    private val _uiState = MutableStateFlow(EditExpensesUiState())
    val uiState: StateFlow<EditExpensesUiState> = _uiState.asStateFlow()

    private val _navigateToExpensesStatScreenEvent = MutableSharedFlow<Unit>()
    val navigateToExpensesStatScreenEvent = _navigateToExpensesStatScreenEvent.asSharedFlow()

    private val repository = ExpensesRepository(DataBaseHolder.dataBase.expensesDao())

    fun showExpenses(){
        viewModelScope.launch {
            _uiState.update {currentState->
                currentState.copy(
                    expenses = loadExpenses()
                )
            }
        }
    }

    private suspend fun loadExpenses():List<EditExpensesAdapter.EditExpenseItem> = repository.getExpenses().map { it.toItem() }

    private fun ExpenseEntity.toItem(): EditExpensesAdapter.EditExpenseItem {
        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.ROOT)
        val uiDate = dateFormat.format(this.dateInMls)
        return EditExpensesAdapter.EditExpenseItem(this.id, this.name, uiDate, this.expensesValue.toDouble())
    }

    fun onStatisticsClicked(){
        viewModelScope.launch {
            _navigateToExpensesStatScreenEvent.emit(Unit)
        }
    }
}