package com.example.piggybank.viewmodels

import androidx.lifecycle.ViewModel
import com.example.piggybank.application.DataBaseHolder
import com.example.piggybank.repository.ExpensesRepository
import com.example.piggybank.uistates.ExpenseStatUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ExpensesObject1ViewModel: ViewModel() {

    private val _expenseState = MutableStateFlow(ExpenseStatUiState())
    val expenseState: StateFlow<ExpenseStatUiState> = _expenseState.asStateFlow()

    private val repository = ExpensesRepository(DataBaseHolder.dataBase.expensesDao())
}